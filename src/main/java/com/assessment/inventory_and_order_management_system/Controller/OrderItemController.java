package com.assessment.inventory_and_order_management_system.Controller;

import com.assessment.inventory_and_order_management_system.Model.OrderItem;
import com.assessment.inventory_and_order_management_system.Service.OrderItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/order-item")
@Slf4j
public class OrderItemController {

    @Autowired
    private OrderItemService orderItemService;

    @PostMapping("/total-orders-for-item")
    public OrderItem getTotalOrdersForItem(@RequestParam Long itemId) throws Exception {
        try {
            log.info("Getting total orders for item: {}", itemId);
            return orderItemService.saveTotalOrdersForItem(itemId);
        } catch (Exception e) {
            log.error("Error getting total orders for item: {}", itemId, e);
            throw new Exception("Error getting total orders for item: " + e.getMessage());
        }
    }

    @PostMapping("/total-orders-for-item-for-user")
    public OrderItem getTotalOrdersForItemForUser(@RequestParam Long itemId,@RequestParam String username) throws Exception {
        try {
            log.info("Getting total orders for item: {}", itemId);
            return orderItemService.saveTotalOrdersForItemForUser(itemId,username);
        } catch (Exception e) {
            log.error("Error getting total orders for item: {}", itemId, e);
            throw new Exception("Error getting total orders for item: " + e.getMessage());
        }
    }

    @GetMapping("/all")
    public Iterable<OrderItem> findAllOrderItems() throws Exception {
        try {
            log.info("Finding all order items");
            return orderItemService.findAllOrderItems();
        } catch (Exception e) {
            throw new Exception("Error finding order items: " + e.getMessage());
        }
    }

    @GetMapping("/item/{itemId}")
    public ResponseEntity<OrderItem> findOrderItemsByItemId(@PathVariable Long itemId) throws Exception {
        try {
            log.info("Finding order items for item: {}", itemId);
            return ResponseEntity.ok(orderItemService.getTotalOrdersForItem(itemId));
        } catch (Exception e) {
            throw new Exception("Error finding order items for item: " + e.getMessage());
        }
    }

    @GetMapping("/user/{itemId}/{username}")
    public ResponseEntity<OrderItem> findOrderItemsByItemIdForUser(@PathVariable Long itemId,@PathVariable String username) throws Exception {
        try {
            log.info("Finding order items for item: {}", itemId);
            return ResponseEntity.ok(orderItemService.getTotalOrdersForItemForUser(itemId,username));
        } catch (Exception e) {
            throw new Exception("Error finding order items for item: " + e.getMessage());
        }
    }


}
