package com.easykitchen.project.service;

import com.easykitchen.project.dao.OrderDao;
import com.easykitchen.project.exception.NonExistingCustomer;
import com.easykitchen.project.exception.ValidationException;
import com.easykitchen.project.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

@Service
public class OrderService {

    private static final String ALPHABET = "abcdefghijklmnopqrstuvwxyzABCDEFHIJKLMNOPQRSTUVWXYZ";
    private static final Random RANDOM = new Random();

    private final OrderDao orderDao;

    private final UserService userService;
    private final RecipeService recipeService;

    @Autowired
    public OrderService(OrderDao orderDao, UserService userService, RecipeService recipeService) {
        this.orderDao = orderDao;
        this.userService = userService;
        this.recipeService = recipeService;
    }

    @Transactional
    public Order createOrder(User user, Integer amount, Category category) {
        Objects.requireNonNull(user);
        Objects.requireNonNull(category);
        checkAmount(amount);
        List<Recipe> recipeList = recipeService.findAllByAmount(category, amount);
        if (recipeList == null) {
            return null;
        }
        Order order = new Order();
        List<OrderItem> orderItems = new ArrayList<>();
        for (Recipe r : recipeList) {
            orderItems.add(new OrderItem(r));
        }
        processOrder(order, user, orderItems);
        orderDao.persist(order);
        return order;
    }

    private void checkAmount(Integer amount) {
        if (amount != 2 && amount != 4) {
            throw new ValidationException("Number of items should be 2 or 4!");
        }
    }

    private void processOrder(Order order, User user, List<OrderItem> orderItems) {
        order.setId(new Random().nextInt());
        order.setCustomer(user);
        order.setCreated(LocalDateTime.now());
        order.setItems(orderItems);
        order.setNumberOfItems(orderItems.size());
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

    @Transactional
    public Order find(Integer id) {
        return orderDao.find(id);
    }

    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public List<Order> findAll() {
        return orderDao.findAll();
    }
}
