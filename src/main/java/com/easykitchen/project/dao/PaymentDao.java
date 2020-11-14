package com.easykitchen.project.dao;


import com.easykitchen.project.model.Payment;

public class PaymentDao extends BaseDao<Payment> {
    public PaymentDao() {
        super(Payment.class);
    }
}
