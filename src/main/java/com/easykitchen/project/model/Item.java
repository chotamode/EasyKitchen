package com.easykitchen.project.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "ITEM_TYPE")
public abstract class Item extends AbstractEntity {

    @Basic(optional = false)
    @Column(nullable = false)
    private Integer amount;

    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private Recipe recipe;

    @Override
    public String toString() {
        return "Item{" +
                "amount=" + amount +
                ", product=" + recipe +
                "}";
    }
}