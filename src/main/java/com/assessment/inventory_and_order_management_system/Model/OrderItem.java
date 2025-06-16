package com.assessment.inventory_and_order_management_system.Model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@Entity
@Table(name = "order_item")
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "item_id")
    private Item item;

    private long quantity;

    public OrderItem(Item item, long quantity) {
        this.item = item;
        this.quantity = quantity;
    }
}
