package com.assessment.inventory_and_order_management_system.Model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Item> items;
    @Column(precision = 10, scale = 2, nullable = false)
    private BigDecimal totalPrice;
    @Column(nullable = false)
    private LocalDateTime timestamp;

    public Order(User user, List<Item> items, BigDecimal totalPrice) {
        this.user = user;
        this.items = items;
        this.totalPrice = totalPrice;
        this.timestamp = LocalDateTime.now();
    }
}
