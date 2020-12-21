package com.easykitchen.project.rest;

import com.easykitchen.project.exception.NotFoundException;
import com.easykitchen.project.model.Cart;
import com.easykitchen.project.model.Order;
import com.easykitchen.project.model.Role;
import com.easykitchen.project.rest.util.RestUtils;
import com.easykitchen.project.security.model.AuthenticationToken;
import com.easykitchen.project.service.OrderService;
import com.easykitchen.project.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/rest/orders")
@PreAuthorize("permitAll()")
public class OrderController {
    private final OrderService orderService;

    private final UserService userService;

    @Autowired
    public OrderController(OrderService orderService, UserService userService) {
        this.orderService = orderService;
        this.userService = userService;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> createOrder(Principal principal, @RequestBody(required = false) Cart cart) {
        final Authentication auth = (Authentication) principal;
        final Cart forOrder;
        if (auth != null && auth.isAuthenticated()) {
            forOrder = userService.getCurrentUserCart();
        } else {
            forOrder = cart;
        }
        final Order order = orderService.create(forOrder);
        final HttpHeaders headers = RestUtils.createLocationHeaderFromCurrentUri("/{id}", order.getId());
        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }

    // Overrides class-level authorization settings
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER', 'ROLE_GUEST')")
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Order getOrder(Principal principal, @PathVariable Integer id) {
        final Order order = orderService.find(id);
        if (order == null) {
            throw NotFoundException.create("Order", id);
        }
        final AuthenticationToken auth = (AuthenticationToken) principal;
        if (auth.getPrincipal().getUser().getRole() != Role.ADMIN &&
                !order.getCustomer().getId().equals(auth.getPrincipal().getUser().getId())) {
            throw new AccessDeniedException("Cannot access order of another customer");
        }
        return order;
    }
}
