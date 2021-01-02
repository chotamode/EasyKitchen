package com.easykitchen.project.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter @Setter
@NamedQueries({
        @NamedQuery(name = "Recipe.findByCategory", query = "SELECT r from Recipe r WHERE :category MEMBER OF r.categories AND NOT r.removed")
})
public class Category extends AbstractEntity {

    @Basic(optional = false)
    @Column(nullable = false)
    private String name;

    @Override
    public String toString() {
        return "Category{" +
                "name='" + name + '\'' +
                "}";
    }
}
