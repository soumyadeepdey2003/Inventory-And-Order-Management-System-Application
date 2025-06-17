package com.assessment.inventory_and_order_management_system.Model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItems = new ArrayList<>();

    @Column(precision = 10, scale = 2, nullable = false)
    private BigDecimal totalPrice;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    // Helper method to add items with quantity
    public void addItem(Item item, long quantity) {
        OrderItem orderItem = new OrderItem(item, quantity);
        orderItem.setOrder(this);
        orderItems.add(orderItem);
    }

    public Order(User user, List<Item> items, BigDecimal totalPrice) {
        this.user = user;
        this.totalPrice = totalPrice;
        this.timestamp = LocalDateTime.now();

        if (items != null) {
            for (Item item : items) {
                addItem(item, 1); // Default quantity of 1 for each item
            }
        }
    }
}
