package com.assessment.inventory_and_order_management_system.Service;

import com.assessment.inventory_and_order_management_system.Dto.OrderDTO;

import java.util.List;

public interface OrderService {
	OrderDTO saveOrder(OrderDTO orderDTO);
	OrderDTO findOrderById(Long id);
	List<OrderDTO> findAllOrders();
	List<OrderDTO> findOrdersByUserId(Long userId);
	void deleteOrder(Long id);
}