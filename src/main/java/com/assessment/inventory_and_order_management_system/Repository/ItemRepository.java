package com.assessment.inventory_and_order_management_system.Repository;

import com.assessment.inventory_and_order_management_system.Model.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
    List<Item> findByNameContainingIgnoreCase(String name);
    boolean existsByName(String name);
}
