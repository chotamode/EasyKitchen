package com.easykitchen.project.dao;

import org.springframework.stereotype.Repository;
import com.easykitchen.project.model.Order;

@Repository
public class OrderDao extends BaseDao<Order> {
    public OrderDao() {
        super(Order.class);
    }
}

