package com.assessment.inventory_and_order_management_system.Repository;

import com.assessment.inventory_and_order_management_system.Model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    List<OrderItem> findByItemId(Long itemId);
    List<OrderItem> findByOrderId(Long orderId);

}
