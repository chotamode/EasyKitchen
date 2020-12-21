package com.easykitchen.project.service;

import com.easykitchen.project.dao.CartDao;
import com.easykitchen.project.dao.OrderDao;
import com.easykitchen.project.exception.NonExistingCustomer;
import com.easykitchen.project.model.Cart;
import com.easykitchen.project.model.Order;
import com.easykitchen.project.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Random;

@Service
public class OrderService {

    private static final String ALPHABET = "abcdefghijklmnopqrstuvwxyzABCDEFHIJKLMNOPQRSTUVWXYZ";
    private static final Random RANDOM = new Random();

    private final OrderDao orderDao;

    private final CartDao cartDao;
    private final UserService userService;

    @Autowired
    public OrderService(OrderDao orderDao, CartDao cartDao,UserService userService) {
        this.orderDao = orderDao;
        this.cartDao = cartDao;
        this.userService = userService;
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
    private User generateCustomerAccount() {
        final User user = new User();
        user.setFirstName("Customer");
        user.setLastName("No" + System.currentTimeMillis());
        user.setUsername("customer-" + System.currentTimeMillis() + "easykitchen.cz");
        final StringBuilder sb = new StringBuilder(5);
        for (int i = 0; i < 5; i++) {
            sb.append(ALPHABET.charAt(RANDOM.nextInt(ALPHABET.length())));
        }
        user.setPassword(sb.toString());
        userService.createUser(user);
        return user;
    }

    private void removeCartItems(Cart cart) {
        cart.getItems().clear();
        cartDao.update(cart);
    }
    @Transactional
    public Order create(Cart cart) {
        Objects.requireNonNull(cart);
        final Order order = new Order(cart);
        if (cart.getOwner() == null) {
            order.setCustomer(generateCustomerAccount());
        }
        order.setCreated(LocalDateTime.now());
        orderDao.persist(order);
        clearCart(cart);
        return order;
    }
    private void clearCart(Cart cart) {
        cart.getItems().clear();
        cartDao.update(cart);
    }
    @Transactional
    public Order find(Integer id) {
        return orderDao.find(id);
    }
}
