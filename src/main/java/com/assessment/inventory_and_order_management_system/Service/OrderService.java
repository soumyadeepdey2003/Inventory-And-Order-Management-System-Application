package com.assessment.inventory_and_order_management_system.Service;


import com.assessment.inventory_and_order_management_system.Model.Order;
import com.assessment.inventory_and_order_management_system.Repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    public Order saveOrder(Order order) throws Exception {
        try {
            log.info("Saving order: {}", order);
            return orderRepository.save(order);
        } catch (Exception e) {
            throw new Exception("Error saving order: " + e.getMessage());
        }
    }

    public Order findOrderById(Long id) throws Exception {
        try {
            log.info("Finding order by id: {}", id);
            return orderRepository.findById(id).orElse(null);
        } catch (Exception e) {
            throw new Exception("Error finding order: " + e.getMessage());
        }
    }

    public List<Order> findAllOrders() throws Exception {
        try {
            log.info("Finding all orders");
            return orderRepository.findAll();
        } catch (Exception e) {
            throw new Exception("Error finding orders: " + e.getMessage());
        }
    }

    public void deleteOrder(Long id) throws Exception {
        try {
            log.info("Deleting order with id: {}", id);
            orderRepository.deleteById(id);
        } catch (Exception e) {
            throw new Exception("Error deleting order: " + e.getMessage());
        }
    }
}
