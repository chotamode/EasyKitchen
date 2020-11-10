package com.easykitchen.project.model;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name="PAYMENT")
public class Payment extends AbstractEntity{
    private Date paid;
    private Double total;
    private String details;

    @OneToOne
    private Order order;

    public Payment(){
    }

    public Payment(Order order){
        this.order = order;
        this.total = order.getTotal();
    }

    public Double getTotal() {
        return total;
    }

    public Date getPaid() {
        return paid;
    }

    public void setPaid(Date paid) {
        this.paid = paid;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }
}
