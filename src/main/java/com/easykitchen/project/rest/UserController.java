package com.easykitchen.project.rest;

import com.easykitchen.project.model.Order;
import com.easykitchen.project.model.User;
import com.easykitchen.project.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
