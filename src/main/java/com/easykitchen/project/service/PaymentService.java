package com.easykitchen.project.service;

import com.easykitchen.project.dao.PaymentDao;
import com.easykitchen.project.model.Order;
import com.easykitchen.project.model.Payment;
import com.easykitchen.project.model.Recipe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
public class PaymentService {

    private final PaymentDao paymentDao;

    @Autowired
    public PaymentService(PaymentDao paymentDao) {
        this.paymentDao = paymentDao;
    }

    @Transactional
    public void makePayment(Order order) {
        Objects.requireNonNull(order);
        final Payment payment = new Payment(order);
        payment.setPaid(LocalDateTime.now());
        payment.setCustomer(order.getCustomer());
        payment.setTotal(order.getTotal());
        paymentDao.persist(payment);
    }

    public List<Payment> findAll() {
        return paymentDao.findAll();
    }

    public Payment find(Integer paymentId) {
        return paymentDao.find(paymentId);
    }

    public void remove(Payment payment) {
        Objects.requireNonNull(payment);
        payment.setCancelled(true);
        paymentDao.update(payment);
    }
}
