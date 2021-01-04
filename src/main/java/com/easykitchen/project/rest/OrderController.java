package com.easykitchen.project.rest;

import com.easykitchen.project.exception.NotFoundException;
import com.easykitchen.project.model.*;
import com.easykitchen.project.security.model.AuthenticationToken;
import com.easykitchen.project.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/rest/orders")
@PreAuthorize("permitAll()")
public class OrderController {
    private final OrderService orderService;

    private final UserService userService;
    private final CategoryService categoryService;
    private final PaymentService paymentService;
    private final RecipeService recipeService;

    @Autowired
    public OrderController(OrderService orderService, UserService userService, CategoryService categoryService, PaymentService paymentService, RecipeService recipeService) {
        this.orderService = orderService;
        this.userService = userService;
        this.categoryService = categoryService;
        this.paymentService = paymentService;
        this.recipeService = recipeService;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Order> getOrders() {
        return orderService.findAll();
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER', 'ROLE_GUEST')")
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Order getOrder(@PathVariable Integer id) {
        final Order order = orderService.find(id);
        if (order == null) {
            throw NotFoundException.create("Order", id);
        }
        return order;
    }

    @PreAuthorize("hasAnyRole('ROLE_USER')")
    @PostMapping(value = "/create/{categoryId}/{amount}")
    public ResponseEntity<Map> createOrder(@PathVariable Integer categoryId,
                                            @PathVariable Integer amount) {
        String userName = (String) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
        User user = userService.getUser(userName);
        Category category = categoryService.find(categoryId);
        Order order = orderService.createOrder(user, amount, category);
        if (order == null) {
            return new ResponseEntity<>(null, null, HttpStatus.BAD_REQUEST);
        }
        Map<String, Object> returnMap = new HashMap<>();
        returnMap.put("id", order.getId());
        returnMap.put("total", order.getTotal());
        returnMap.put("items", order.getItems());
        return new ResponseEntity<>(returnMap, null, HttpStatus.CREATED);
    }

    @PreAuthorize("hasAnyRole('ROLE_USER')")
    @GetMapping(value = "/pay/{orderId}")
    public ResponseEntity<Void> payOrder(@PathVariable Integer orderId) {
        paymentService.makePayment(orderService.find(orderId));
        for (OrderItem i: orderService.find(orderId).getItems()) {
            i.getRecipe().setAmount(i.getRecipe().getAmount() - i.getAmount());
            recipeService.update(i.getRecipe());
        }
        return new ResponseEntity<>(null, HttpStatus.CREATED);
    }
}
