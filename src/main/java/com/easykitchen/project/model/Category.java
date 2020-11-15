package com.easykitchen.project.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import java.util.List;

@Entity
@Getter @Setter
public class Category extends AbstractEntity {

    private String name;

    private List<Recipe> recipes;

    @Override
    public String toString() {
        return "Category{" +
                "name='" + name + '\'' +
                "}";
    }
}
