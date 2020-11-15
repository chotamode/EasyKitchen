package com.easykitchen.project.service;

import com.easykitchen.project.dao.CartDao;
import com.easykitchen.project.dao.OrderDao;
import com.easykitchen.project.exception.NonExistingCustomer;
import com.easykitchen.project.model.Cart;
import com.easykitchen.project.model.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Random;

@Service
public class OrderService {

    private final OrderDao orderDao;

    private final CartDao cartDao;

    @Autowired
    public OrderService(OrderDao orderDao, CartDao cartDao) {
        this.orderDao = orderDao;
        this.cartDao = cartDao;
    }

    @Transactional
    public Order createOrder(Cart cart) {
        Objects.requireNonNull(cart);
        checkCartCustomer(cart);
        Order order = new Order(cart);
        order.setId(new Random().nextInt());
        order.setCreated(LocalDateTime.now());
        removeCartItems(cart);
        orderDao.persist(order);
        return order;
    }

    private void checkCartCustomer(Cart cart) {
        if (cart.getOwner() == null) {
            throw new NonExistingCustomer("Cart should have owner, otherwise order cannot be completed");
        }
    }

    private void removeCartItems(Cart cart) {
        cart.getItems().clear();
        cartDao.update(cart);
    }
}
