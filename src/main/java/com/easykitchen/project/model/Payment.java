package com.easykitchen.project.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Getter @Setter @NoArgsConstructor
@Table(name="PAYMENT")
@NamedQueries({
        @NamedQuery(name = "Payment.findByUser", query = "SELECT p from Payment p WHERE :customer = p.customer")
})
public class Payment extends AbstractEntity{
    private LocalDateTime paid;
    private Double total;
    private String details;

    @ManyToOne
    private User customer;

    @OneToOne
    private Order order;

    public Payment(Order order) {
        this.order = order;
        this.customer = order.getCustomer();
        this.total = order.getTotal();
    }
}
