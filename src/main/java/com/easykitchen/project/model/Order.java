package com.easykitchen.project.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Getter @Setter @NoArgsConstructor
@Table(name = "SHOP_ORDER")
public class Order extends AbstractEntity {

    @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime created;

    @ManyToOne
    @JoinColumn(nullable = false)
    private User customer;

    @OneToMany(cascade = CascadeType.PERSIST, orphanRemoval = true)
    @JoinColumn(name = "order_id")
    private List<OrderItem> items;

    public Order(Cart cart) {
        this.customer = cart.getOwner();
        assert cart.getItems() != null;
        this.items = cart.getItems().stream().map(OrderItem::new).collect(Collectors.toList());
    }

    public Double getTotal() {
        Double total = 0.0;
        for (OrderItem i:items
             ) {
            total += i.getRecipe().getPrice();
        }
        return total;
    }

    @Override
    public String toString() {
        return "Order{" +
                "created=" + created +
                ", customer=" + customer +
                "}";
    }
}
