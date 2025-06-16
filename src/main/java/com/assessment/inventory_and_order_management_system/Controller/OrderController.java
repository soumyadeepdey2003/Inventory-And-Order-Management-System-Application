package com.assessment.inventory_and_order_management_system.Controller;


import com.assessment.inventory_and_order_management_system.Model.Order;
import com.assessment.inventory_and_order_management_system.Service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/order")
@Slf4j
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/save")
    public ResponseEntity<Order> saveOrder( @RequestBody Order order) throws Exception {
        try {
            log.info("Saving order: {}", order);
            return ResponseEntity.ok(orderService.saveOrder(order));
        } catch (Exception e) {
            throw new Exception("Error saving order: " + e.getMessage());
        }
    }

    @GetMapping("/find/{id}")
    public ResponseEntity<Order> findOrderById(@PathVariable Long id) throws Exception {
        try {
            log.info("Finding order by id: {}", id);
            return ResponseEntity.ok(orderService.findOrderById(id));
        } catch (Exception e) {
            throw new Exception("Error finding order: " + e.getMessage());
        }
    }

    @GetMapping("/all")
    public ResponseEntity<Iterable<Order>> findAllOrders() throws Exception {
        try {
            log.info("Finding all orders");
            return ResponseEntity.ok(orderService.findAllOrders());
        } catch (Exception e) {
            throw new Exception("Error finding orders: " + e.getMessage());
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) throws Exception {
        try {
            log.info("Deleting order by id: {}", id);
            orderService.deleteOrder(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            throw new Exception("Error deleting order: " + e.getMessage());
        }
    }
}
