package com.assessment.inventory_and_order_management_system.Service.Impl;

import com.assessment.inventory_and_order_management_system.Model.Item;
import com.assessment.inventory_and_order_management_system.Model.Order;
import com.assessment.inventory_and_order_management_system.Model.User;
import com.assessment.inventory_and_order_management_system.Repository.ItemRepository;
import com.assessment.inventory_and_order_management_system.Repository.OrderRepository;
import com.assessment.inventory_and_order_management_system.Repository.UserRepository;
import com.assessment.inventory_and_order_management_system.Service.OrderService;
import com.assessment.inventory_and_order_management_system.Dto.OrderDTO;
import com.assessment.inventory_and_order_management_system.Exception.InsufficientInventoryException;
import com.assessment.inventory_and_order_management_system.Exception.InvalidRequestException;
import com.assessment.inventory_and_order_management_system.Exception.ResourceNotFoundException;
import com.assessment.inventory_and_order_management_system.Mapper.OrderMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final ItemRepository itemRepository;

    @Override
    @Transactional
    @PreAuthorize("isAuthenticated()")
    public OrderDTO saveOrder(OrderDTO orderDTO) {
        log.info("Saving order: {}", orderDTO);

        // Validate order
        if (orderDTO.getItemIds() == null || orderDTO.getItemIds().isEmpty()) {
            throw new InvalidRequestException("Order must contain at least one item");
        }

        // Calculate total price if not provided
        if (orderDTO.getTotalPrice() == null || orderDTO.getTotalPrice().compareTo(BigDecimal.ZERO) == 0) {
            BigDecimal totalPrice = calculateTotalPrice(orderDTO.getItemIds());
            orderDTO.setTotalPrice(totalPrice);
        }

        // Get the user
        User user = userRepository.findById(orderDTO.getUserId())
            .orElseThrow( () -> ResourceNotFoundException.forId("User", orderDTO.getUserId()));

        // Create order entity directly over here
        Order order = new Order();
        order.setUser(user);
        order.setTotalPrice(orderDTO.getTotalPrice());
        order.setTimestamp(orderDTO.getTimestamp());

        // Add items to the order with proper quantities
        for (Long itemId : orderDTO.getItemIds()) {
            Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> ResourceNotFoundException.forId("Item", itemId));

            if (item.getQuantity() <= 0) {
                throw InsufficientInventoryException.forItem(item.getName());
            }

            item.setQuantity(item.getQuantity() - 1); // Decrease item quantity
            itemRepository.save(item); // Save updated item

            // Add item to order with quantity of 1
            order.addItem(item, 1);
        }

        // Save the order
        Order savedOrder = orderRepository.save(order);
        log.info("Order saved with ID: {}", savedOrder.getId());
        return orderMapper.toDTO(savedOrder);
    }

    @Override
    @PreAuthorize("hasAnyRole('ADMIN') or @userSecurity.canAccessOrder(#id)")
    public OrderDTO findOrderById(Long id) {
        log.info("Finding order by id: {}", id);

        Order order = orderRepository.findById(id)
            .orElseThrow(() -> ResourceNotFoundException.forId("Order", id));

        return orderMapper.toDTO(order);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public List<OrderDTO> findAllOrders() {
        log.info("Finding all orders");

        List<Order> orders = orderRepository.findAll();
        return orders.stream()
            .map(orderMapper::toDTO)
            .toList();
    }

    @Override
    @PreAuthorize("hasRole('ADMIN') or @userSecurity.isCurrentUser(#userId)")
    public List<OrderDTO> findOrdersByUserId(Long userId) {
        log.info("Finding orders by userId: {}", userId);

        List<Order> orders = orderRepository.findByUserId(userId);
        return orders.stream()
            .map(orderMapper::toDTO)
            .toList();
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteOrder(Long id) {
        log.info("Deleting order with id: {}", id);

        // Check if order exists
        orderRepository.findById(id)
            .orElseThrow(() -> ResourceNotFoundException.forId("Order", id));

        orderRepository.deleteById(id);
        log.info("Order deleted with id: {}", id);
    }

    private BigDecimal calculateTotalPrice(List<Long> itemIds) {
        return itemIds.stream()
            .map(id -> itemRepository.findById(id)
                .orElseThrow(() -> ResourceNotFoundException.forId("Item", id)))
            .map(Item::getPrice)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}