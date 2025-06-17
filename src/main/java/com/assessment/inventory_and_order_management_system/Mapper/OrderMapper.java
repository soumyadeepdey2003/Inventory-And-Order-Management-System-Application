package com.assessment.inventory_and_order_management_system.Mapper;

import com.assessment.inventory_and_order_management_system.Exception.ResourceNotFoundException;
import com.assessment.inventory_and_order_management_system.Model.Item;
import com.assessment.inventory_and_order_management_system.Model.Order;
import com.assessment.inventory_and_order_management_system.Model.User;
import com.assessment.inventory_and_order_management_system.Repository.ItemRepository;
import com.assessment.inventory_and_order_management_system.Repository.UserRepository;
import com.assessment.inventory_and_order_management_system.Dto.OrderDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class OrderMapper {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ItemRepository itemRepository;

	@Autowired
	private ItemMapper itemMapper;

	public OrderDTO toDTO(Order order) {
		if (order == null) {
			return null;
		}

		// Extract item IDs from order items
		List<Long> itemIds = order.getOrderItems().stream()
				.map(orderItem -> orderItem.getItem().getId())
				.toList();

		return OrderDTO.builder()
				.id(order.getId())
				.userId(order.getUser().getId())
				.username(order.getUser().getUsername())
				.totalPrice(order.getTotalPrice())
				.timestamp(order.getTimestamp())
				.itemIds(itemIds)
				.build();
	}

	public Order toEntity(OrderDTO orderDTO) {
		if (orderDTO == null) {
			return null;
		}

		User user = userRepository.findById(orderDTO.getUserId())
				.orElseThrow(() -> ResourceNotFoundException.forId("User", orderDTO.getUserId()));

		Order order = new Order();
		order.setId(orderDTO.getId());
		order.setUser(user);
		order.setTotalPrice(orderDTO.getTotalPrice());

		// Ensure timestamp is set
		if (orderDTO.getTimestamp() != null) {
			order.setTimestamp(orderDTO.getTimestamp());
		} else {
			order.setTimestamp(LocalDateTime.now());
		}

		// Add items to order with proper OrderItem relationship
		if (orderDTO.getItemIds() != null) {
			for (Long itemId : orderDTO.getItemIds()) {
				Item item = itemRepository.findById(itemId)
						.orElseThrow(() -> ResourceNotFoundException.forId("Item", itemId));
				order.addItem(item, 1);
			}
		}

		return order;
	}
}