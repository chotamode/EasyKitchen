package com.easykitchen.project.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class RecipeIngredient extends AbstractEntity {

    @ManyToOne
    private StorageIngredient StorageIngredient;

    private String name;

    private Integer amount;

    private Unit unit;
}
