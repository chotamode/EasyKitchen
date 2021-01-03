package com.easykitchen.project.rest;

import com.easykitchen.project.exception.NotFoundException;
import com.easykitchen.project.model.Category;
import com.easykitchen.project.model.OrderItem;
import com.easykitchen.project.model.Payment;
import com.easykitchen.project.model.Recipe;
import com.easykitchen.project.service.PaymentService;
import com.easykitchen.project.service.RecipeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rest/payments")
public class PaymentController {

    private static final Logger LOG = LoggerFactory.getLogger(PaymentController.class);

    PaymentService paymentService;
    RecipeService recipeService;

    public PaymentController(PaymentService paymentService, RecipeService recipeService){
        this.paymentService = paymentService;
        this.recipeService = recipeService;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Payment> getPayments() {
        return paymentService.findAll();
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    @DeleteMapping(value = "/{paymentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeProductFromCategory(@PathVariable Integer paymentId) {
        final Payment toRemove = paymentService.find(paymentId);
        if (toRemove == null) {
            throw NotFoundException.create("Payment", paymentId);
        }
        for (OrderItem i: toRemove.getOrder().getItems()
        ) {
            i.getRecipe().setAmount(i.getRecipe().getAmount() + i.getAmount());
            recipeService.update(i.getRecipe());
        }
        paymentService.remove(toRemove);
        LOG.debug("Payment {} cancelled", toRemove);
    }

}
