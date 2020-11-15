package com.easykitchen.project.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Getter @Setter @NoArgsConstructor
@Table(name="PAYMENT")
public class Payment extends AbstractEntity{
    private Date paid;
    private Double total;
    private String details;

    @OneToOne
    private Order order;

    public Payment(Order order){
        this.order = order;
        this.total = order.getTotal();
    }
}
