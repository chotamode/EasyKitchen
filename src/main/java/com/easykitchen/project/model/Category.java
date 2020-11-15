package com.easykitchen.project.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;

@Entity
@Getter @Setter
public class Category extends AbstractEntity {

    private String name;

    @Override
    public String toString() {
        return "Category{" +
                "name='" + name + '\'' +
                "}";
    }
}
