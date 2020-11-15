package com.easykitchen.project.dao;

import com.easykitchen.project.model.User;
import org.springframework.stereotype.Repository;
import com.easykitchen.project.model.Order;

import java.util.List;
import java.util.Objects;

@Repository
public class OrderDao extends BaseDao<Order> {
    public OrderDao() {
        super(Order.class);
    }

    public List<Order> findAll(User user) {
        Objects.requireNonNull(user);
        return em.createNamedQuery("Order.findByUser", Order.class).setParameter("customer", user)
                .getResultList();
    }
}

