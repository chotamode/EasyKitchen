package com.easykitchen.project.rest;

import com.easykitchen.project.model.Order;
import com.easykitchen.project.model.Payment;
import com.easykitchen.project.model.User;
import com.easykitchen.project.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController()
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER', 'ROLE_GUEST')")
    @GetMapping("/current")
    public User getUserInfo() {
        String userName = (String) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
        return userService.getUser(userName);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER', 'ROLE_GUEST')")
    @GetMapping("/current/orders")
    public List<Order> getAllOrders() {
        String userName = (String) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
        return userService.getAllOrders(userName);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER', 'ROLE_GUEST')")
    @GetMapping("/current/payments")
    public List<Payment> getAllPayments() {
        String userName = (String) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
        return userService.getAllPayments(userName);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER', 'ROLE_GUEST')")
    @PutMapping("/current/edit")
    public ResponseEntity<Void> editUser(@RequestBody User user) {
        userService.updateUser(user);
        return new ResponseEntity<>(null, HttpStatus.OK);
    }
}