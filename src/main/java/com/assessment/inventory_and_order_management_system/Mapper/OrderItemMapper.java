package com.assessment.inventory_and_order_management_system.Mapper;

import com.assessment.inventory_and_order_management_system.Model.OrderItem;
import com.assessment.inventory_and_order_management_system.Repository.ItemRepository;
import com.assessment.inventory_and_order_management_system.Repository.OrderRepository;
import com.assessment.inventory_and_order_management_system.Dto.OrderItemDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OrderItemMapper {

	@Autowired
	private ItemRepository itemRepository;

	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private ItemMapper itemMapper;

	public OrderItemDTO toDTO(OrderItem orderItem) {
		if (orderItem == null) {
			return null;
		}

		OrderItemDTO orderItemDTO = OrderItemDTO.builder()
				.id(orderItem.getId())
				.quantity(orderItem.getQuantity())
				.build();

		if (orderItem.getItem() != null) {
			orderItemDTO.setItemId(orderItem.getItem().getId());
			orderItemDTO.setItem(itemMapper.toDTO(orderItem.getItem()));
		}

		if (orderItem.getOrder() != null) {
			orderItemDTO.setOrderId(orderItem.getOrder().getId());
		}

		return orderItemDTO;
	}

	public OrderItem toEntity(OrderItemDTO orderItemDTO) {
		if (orderItemDTO == null) {
			return null;
		}

		OrderItem orderItem = new OrderItem();
		orderItem.setId(orderItemDTO.getId());
		orderItem.setQuantity(orderItemDTO.getQuantity());

		if (orderItemDTO.getItemId() != null) {
			orderItem.setItem(itemRepository.findById(orderItemDTO.getItemId())
					.orElseThrow(() -> new RuntimeException("Item not found with id: " + orderItemDTO.getItemId())));
		}

		if (orderItemDTO.getOrderId() != null) {
			orderItem.setOrder(orderRepository.findById(orderItemDTO.getOrderId())
					.orElseThrow(() -> new RuntimeException("Order not found with id: " + orderItemDTO.getOrderId())));
		}

		return orderItem;
	}
}