package com.assessment.inventory_and_order_management_system.Service;

import com.assessment.inventory_and_order_management_system.Dto.OrderItemDTO;

import java.util.List;

public interface OrderItemService {
	OrderItemDTO getTotalOrdersForItem(Long itemId);
	OrderItemDTO getTotalOrdersForItemForUser(Long itemId, String username);
	OrderItemDTO saveTotalOrdersForItemForUser(Long itemId, String username);
	OrderItemDTO saveTotalOrdersForItem(Long itemId);
	List<OrderItemDTO> findAllOrderItems();
	List<OrderItemDTO> findOrderItemsByItemId(Long itemId);
}