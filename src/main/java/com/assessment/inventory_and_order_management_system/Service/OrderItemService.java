package com.assessment.inventory_and_order_management_system.Service;


import com.assessment.inventory_and_order_management_system.Model.Item;
import com.assessment.inventory_and_order_management_system.Model.Order;
import com.assessment.inventory_and_order_management_system.Model.OrderItem;
import com.assessment.inventory_and_order_management_system.Repository.ItemRepository;
import com.assessment.inventory_and_order_management_system.Repository.OrderItemRepository;
import com.assessment.inventory_and_order_management_system.Repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderItemService {

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ItemRepository itemRepository;


  public OrderItem getTotalOrdersForItem(Long itemId) {
      Item item = itemRepository.findById(itemId).orElseThrow(()-> new RuntimeException("Item not found"));
      List<Order> orders = orderRepository.findByItems(item);
      long quantity = 0;
      for (Order order : orders) {
          quantity += order.getItems().size();
      }
      OrderItem orderItem = new OrderItem(item, quantity);
      return orderItem;
  }

    public OrderItem getTotalOrdersForItemForUser(Long itemId,String username) {
        Item item = itemRepository.findById(itemId).orElseThrow(()-> new RuntimeException("Item not found"));
        List<Order> orders = orderRepository.findByItems(item);
        long quantity = 0;
        for (Order order : orders) {
            if(order.getUser().getUsername().equals(username))
                quantity += order.getItems().size();
        }
        return new OrderItem(item, quantity);
    }

    public OrderItem saveTotalOrdersForItemForUser(Long itemId,String username) {
        return orderItemRepository.save(getTotalOrdersForItemForUser(itemId,username));
    }

    public OrderItem saveTotalOrdersForItem(Long itemId) {
        return orderItemRepository.save(getTotalOrdersForItem(itemId));
    }


    public Iterable<OrderItem> findAllOrderItems() throws Exception {
        try {
            log.info("Finding all order items");
            return orderItemRepository.findAll();
        } catch (Exception e) {
            throw new Exception("Error finding order items: " + e.getMessage());
        }
    }
}
