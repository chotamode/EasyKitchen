package com.easykitchen.project.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;

@Entity
@Getter
@Setter
public class Ingredient extends AbstractEntity {
    private String name;
    private Integer amount;
    private Unit unit;
}
