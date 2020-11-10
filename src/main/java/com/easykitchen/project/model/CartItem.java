package com.easykitchen.project.model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("CART")
public class CartItem extends Item {
}
