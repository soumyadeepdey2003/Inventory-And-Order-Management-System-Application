package com.assessment.inventory_and_order_management_system.Controller;

import com.assessment.inventory_and_order_management_system.Service.OrderItemService;
import com.assessment.inventory_and_order_management_system.Dto.OrderItemDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/order-items")
@Slf4j
public class OrderItemController {

    private final OrderItemService orderItemService;

    @GetMapping
    public ResponseEntity<List<OrderItemDTO>> getAllOrderItems() {
        log.info("REST request to get all OrderItems");
        List<OrderItemDTO> orderItems = orderItemService.findAllOrderItems();
        return ResponseEntity.ok(orderItems);
    }

    @GetMapping("/item/{itemId}")
    public ResponseEntity<OrderItemDTO> getOrderItemsByItemId(@PathVariable Long itemId) {
        log.info("REST request to get OrderItem by itemId: {}", itemId);
        OrderItemDTO orderItemDTO = orderItemService.getTotalOrdersForItem(itemId);
        return ResponseEntity.ok(orderItemDTO);
    }

    @GetMapping("/item/{itemId}/user/{username}")
    public ResponseEntity<OrderItemDTO> getOrderItemsByItemIdAndUsername(
        @PathVariable Long itemId,
        @PathVariable String username) {
        log.info("REST request to get OrderItem by itemId and username: {}, {}", itemId, username);
        OrderItemDTO orderItemDTO = orderItemService.getTotalOrdersForItemForUser(itemId, username);
        return ResponseEntity.ok(orderItemDTO);
    }

    @PostMapping("/item/{itemId}")
    public ResponseEntity<OrderItemDTO> saveOrderItemByItemId(@PathVariable Long itemId) {
        log.info("REST request to save OrderItem for itemId: {}", itemId);
        OrderItemDTO orderItemDTO = orderItemService.saveTotalOrdersForItem(itemId);
        return ResponseEntity.ok(orderItemDTO);
    }

    @PostMapping("/item/{itemId}/user/{username}")
    public ResponseEntity<OrderItemDTO> saveOrderItemByItemIdAndUsername(
        @PathVariable Long itemId,
        @PathVariable String username) {
        log.info("REST request to save OrderItem for itemId and username: {}, {}", itemId, username);
        OrderItemDTO orderItemDTO = orderItemService.saveTotalOrdersForItemForUser(itemId, username);
        return ResponseEntity.ok(orderItemDTO);
    }
}