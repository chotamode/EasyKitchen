package com.easykitchen.project.model;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Entity
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

    public Double getTotal() {
        Double total = 0.0;
        for (OrderItem i:items
             ) {
            total += i.getProduct().getPrice();
        }
        return total;
    }

    public Order() {
    }

    public Order(Cart cart) {
        this.customer = cart.getOwner();
        assert cart.getItems() != null;
        this.items = cart.getItems().stream().map(OrderItem::new).collect(Collectors.toList());
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    public User getCustomer() {
        return customer;
    }

    public void setCustomer(User customer) {
        this.customer = customer;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public void setItems(List<OrderItem> items) {
        this.items = items;
    }

    @Override
    public String toString() {
        return "Order{" +
                "created=" + created +
                ", customer=" + customer +
                "}";
    }
}
