package com.easykitchen.project.dao;


import com.easykitchen.project.model.Cart;
import org.springframework.stereotype.Repository;

@Repository
public class CartDao extends BaseDao<Cart> {
    protected CartDao() {
        super(Cart.class);
    }
}

