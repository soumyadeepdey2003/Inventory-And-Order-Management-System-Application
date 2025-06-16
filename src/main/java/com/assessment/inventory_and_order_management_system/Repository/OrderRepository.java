package com.assessment.inventory_and_order_management_system.Repository;

import com.assessment.inventory_and_order_management_system.Model.Item;
import com.assessment.inventory_and_order_management_system.Model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUserUsername(String username);
    List<Order> findAllByOrderByTimestampDesc();

    List<Order> findByItems(Item item);
}
