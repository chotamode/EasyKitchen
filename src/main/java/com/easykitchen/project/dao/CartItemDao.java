package com.easykitchen.project.dao;

import com.easykitchen.project.model.CartItem;

import org.springframework.stereotype.Repository;


@Repository
public class CartItemDao extends BaseDao<CartItem> {
    public CartItemDao() {
        super(CartItem.class);
    }
}
