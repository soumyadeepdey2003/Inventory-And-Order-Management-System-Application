package com.assessment.inventory_and_order_management_system.Service.Impl;

import com.assessment.inventory_and_order_management_system.Model.Item;
import com.assessment.inventory_and_order_management_system.Model.Order;
import com.assessment.inventory_and_order_management_system.Model.OrderItem;
import com.assessment.inventory_and_order_management_system.Repository.ItemRepository;
import com.assessment.inventory_and_order_management_system.Repository.OrderItemRepository;
import com.assessment.inventory_and_order_management_system.Repository.OrderRepository;
import com.assessment.inventory_and_order_management_system.Service.OrderItemService;
import com.assessment.inventory_and_order_management_system.Dto.OrderItemDTO;
import com.assessment.inventory_and_order_management_system.Exception.ResourceNotFoundException;
import com.assessment.inventory_and_order_management_system.Mapper.OrderItemMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderItemServiceImpl implements OrderItemService {

  private final OrderItemRepository orderItemRepository;
  private final OrderRepository orderRepository;
  private final ItemRepository itemRepository;
  private final OrderItemMapper orderItemMapper;

  @Override
  @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
  public OrderItemDTO getTotalOrdersForItem(Long itemId) {
    log.info("Getting total orders for item: {}", itemId);

    Item item = itemRepository.findById(itemId)
        .orElseThrow(() -> ResourceNotFoundException.forId("Item", itemId));

    // Get all OrderItems for this Item
    List<OrderItem> orderItems = orderItemRepository.findByItemId(itemId);

    // Sum up the total quantity
    long quantity = orderItems.stream()
        .mapToLong(OrderItem::getQuantity)
        .sum();

    OrderItem orderItem = new OrderItem(item, quantity);
    return orderItemMapper.toDTO(orderItem);
  }

  @Override
  @PreAuthorize("hasRole('ADMIN') or @userSecurity.isCurrentUser(#username)")
  public OrderItemDTO getTotalOrdersForItemForUser(Long itemId, String username) {
    log.info("Getting total orders for item: {} and user: {}", itemId, username);

    Item item = itemRepository.findById(itemId)
        .orElseThrow(() -> ResourceNotFoundException.forId("Item", itemId));

    // Find all orders for the specified user
    List<Order> userOrders = orderRepository.findAll().stream()
        .filter(order -> order.getUser().getUsername().equals(username))
        .collect(Collectors.toList());

    // Calculate total quantity for this item in user's orders
    long quantity = 0;
    for (Order order : userOrders) {
      for (OrderItem orderItem : order.getOrderItems()) {
        if (orderItem.getItem().getId().equals(itemId)) {
          quantity += orderItem.getQuantity();
        }
      }
    }

    OrderItem orderItem = new OrderItem(item, quantity);
    return orderItemMapper.toDTO(orderItem);
  }

  @Override
  @PreAuthorize("hasRole('ADMIN') or @userSecurity.isCurrentUser(#username)")
  public OrderItemDTO saveTotalOrdersForItemForUser(Long itemId, String username) {
    log.info("Saving total orders for item: {} and user: {}", itemId, username);

    OrderItemDTO orderItemDTO = getTotalOrdersForItemForUser(itemId, username);
    OrderItem orderItem = orderItemMapper.toEntity(orderItemDTO);
    OrderItem savedOrderItem = orderItemRepository.save(orderItem);

    return orderItemMapper.toDTO(savedOrderItem);
  }

  @Override
  @PreAuthorize("hasRole('ADMIN')")
  public OrderItemDTO saveTotalOrdersForItem(Long itemId) {
    log.info("Saving total orders for item: {}", itemId);

    OrderItemDTO orderItemDTO = getTotalOrdersForItem(itemId);
    OrderItem orderItem = orderItemMapper.toEntity(orderItemDTO);
    OrderItem savedOrderItem = orderItemRepository.save(orderItem);

    return orderItemMapper.toDTO(savedOrderItem);
  }

  @Override
  @PreAuthorize("hasRole('ADMIN')")
  public List<OrderItemDTO> findAllOrderItems() {
    log.info("Finding all order items");

    List<OrderItem> orderItems = orderItemRepository.findAll();
    return orderItems.stream()
        .map(orderItemMapper::toDTO)
        .toList();
  }

  @Override
  @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
  public List<OrderItemDTO> findOrderItemsByItemId(Long itemId) {
    log.info("Finding order items by item id: {}", itemId);

    // Verify item exists
    itemRepository.findById(itemId)
        .orElseThrow(() -> ResourceNotFoundException.forId("Item", itemId));

    List<OrderItem> orderItems = orderItemRepository.findByItemId(itemId);
    return orderItems.stream()
        .map(orderItemMapper::toDTO)
        .toList();
  }
}