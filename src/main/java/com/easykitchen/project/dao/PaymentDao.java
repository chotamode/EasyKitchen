package com.easykitchen.project.dao;

import com.easykitchen.project.model.Payment;
import com.easykitchen.project.model.User;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;

@Repository
public class PaymentDao extends BaseDao<Payment> {
    public PaymentDao() {
        super(Payment.class);
    }

    public List<Payment> findAll(User user) {
        Objects.requireNonNull(user);
        return em.createNamedQuery("Payment.findByUser", Payment.class).setParameter("customer", user)
                .getResultList();
    }
}
