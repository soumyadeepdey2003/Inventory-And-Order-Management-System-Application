package com.assessment.inventory_and_order_management_system.Controller;

import com.assessment.inventory_and_order_management_system.Service.OrderService;
import com.assessment.inventory_and_order_management_system.Dto.OrderDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/orders")
@Slf4j
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderDTO> createOrder(@Valid @RequestBody OrderDTO orderDTO) {
        log.info("REST request to create Order: {}", orderDTO);
        OrderDTO result = orderService.saveOrder(orderDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderDTO> getOrderById(@PathVariable Long id) {
        log.info("REST request to get Order by id: {}", id);
        OrderDTO orderDTO = orderService.findOrderById(id);
        return ResponseEntity.ok(orderDTO);
    }

    @GetMapping
    public ResponseEntity<List<OrderDTO>> getAllOrders() {
        log.info("REST request to get all Orders");
        List<OrderDTO> orders = orderService.findAllOrders();
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<OrderDTO>> getOrdersByUserId(@PathVariable Long userId) {
        log.info("REST request to get Orders by userId: {}", userId);
        List<OrderDTO> orders = orderService.findOrdersByUserId(userId);
        return ResponseEntity.ok(orders);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        log.info("REST request to delete Order: {}", id);
        orderService.deleteOrder(id);
        return ResponseEntity.noContent().build();
    }
}