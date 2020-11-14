package com.easykitchen.project.model;

import javax.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "ITEM_TYPE")
public abstract class Item extends AbstractEntity {

    @Basic(optional = false)
    @Column(nullable = false)
    private Integer amount;

    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private Recipe recipe;

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Recipe getProduct() {
        return recipe;
    }

    public void setProduct(Recipe recipe) {
        this.recipe = recipe;
    }

    @Override
    public String toString() {
        return "Item{" +
                "amount=" + amount +
                ", product=" + recipe +
                "}";
    }
}