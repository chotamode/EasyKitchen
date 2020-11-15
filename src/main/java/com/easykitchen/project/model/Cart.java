package com.easykitchen.project.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.*;

@Entity
@Getter @Setter
public class Cart extends AbstractEntity {

    @JsonIgnore
    @OneToOne(mappedBy = "cart", optional = false)
    private User owner;

    @OneToMany(cascade = CascadeType.MERGE, orphanRemoval = true)
    @JoinColumn(name = "cart_id")
    private List<CartItem> items;

    public void addItem(CartItem item) {
        Objects.requireNonNull(item);
        if (items == null) {
            this.items = new ArrayList<>();
        }
        final Optional<CartItem> existing = items.stream()
                .filter(it -> it.getRecipe().getId()
                    .equals(item.getRecipe().getId()))
                .findAny();
        if (existing.isPresent()) {
            existing.get().setAmount(existing.get().getAmount() + item.getAmount());
        } else {
            items.add(item);
        }
    }

    public void removeItem(Item item) {
        Objects.requireNonNull(item);
        final Iterator<CartItem> it = items.iterator();
        while (it.hasNext()) {
            final CartItem curr = it.next();
            if (curr.getRecipe().getId().equals(item.getRecipe().getId())) {
                if (item.getAmount() >= curr.getAmount()) {
                    it.remove();
                } else {
                    curr.setAmount(curr.getAmount() - item.getAmount());
                }
                break;
            }
        }
    }

    @Override
    public String toString() {
        return "Cart{" +
                "items=" + items +
                "}";
    }
}
