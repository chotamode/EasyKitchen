package com.easykitchen.project.service;

import com.easykitchen.project.dao.OrderDao;
import com.easykitchen.project.dao.PaymentDao;
import com.easykitchen.project.dao.UserDao;
import com.easykitchen.project.exception.AlreadyExistingUserException;
import com.easykitchen.project.model.Order;
import com.easykitchen.project.model.Payment;
import com.easykitchen.project.model.User;
import com.easykitchen.project.util.BackendConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;

@Service
public class UserService {

    private final UserDao userDao;

    private final OrderDao orderDao;

    private final PaymentDao paymentDao;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserDao userDao, OrderDao orderDao, PaymentDao paymentDao, PasswordEncoder passwordEncoder) {
        this.userDao = userDao;
        this.orderDao = orderDao;
        this.paymentDao = paymentDao;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public void createUser(User user) {
        Objects.requireNonNull(user);
        checkExistingUser(user);
        user.encodePassword(passwordEncoder);
        if (user.getRole() == null) {
            user.setRole(BackendConstants.DEFAULT_ROLE);
        }
        userDao.persist(user);
    }

    @Transactional
    public User getUser(String username) {
        Objects.requireNonNull(username);
        return userDao.findByUsername(username);
    }

    @Transactional
    public List<Order> getAllOrders(String username) {
        Objects.requireNonNull(username);
        final User user = userDao.findByUsername(username);
        if (user != null) {
            return orderDao.findAll(user);
        }
        return null;
    }

    @Transactional
    public List<Payment> getAllPayments(String username) {
        Objects.requireNonNull(username);
        final User user = userDao.findByUsername(username);
        if (user != null) {
            return paymentDao.findAll(user);
        }
        return null;
    }

    private void checkExistingUser(User user) {
        if (userDao.findByUsername(user.getUsername()) != null) {
            throw new AlreadyExistingUserException("User with this username already exists!");
        }
    }


}
