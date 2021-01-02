package com.easykitchen.project.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Getter @Setter
public class Address extends AbstractEntity{
    private String street;
    private String number;
    private String zip;
    private String city;

    @Override
    public String toString() {
        return "Product{" +
                "street='" + street + '\'' +
                ", number=" + number +
                ", zip=" + zip +
                ", city =" + city +
                "}";
    }
}
