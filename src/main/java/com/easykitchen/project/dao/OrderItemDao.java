package com.easykitchen.project.dao;


import com.easykitchen.project.model.OrderItem;

public class OrderItemDao extends BaseDao<OrderItem> {
    public OrderItemDao() {
        super(OrderItem.class);
    }
}
