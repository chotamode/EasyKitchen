package com.easykitchen.project.model;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Getter @Setter
@NamedQueries({
        @NamedQuery(name = "Recipe.findByCategory", query = "SELECT r from Recipe r WHERE :category MEMBER OF r.categories AND NOT r.removed")
})
public class Recipe extends AbstractEntity {

    @Basic(optional = false)
    @Column(nullable = false)
    private String name;

    @Basic(optional = false)
    @Column(nullable = false)
    private Integer amount;

    @Basic(optional = false)
    @Column(nullable = false)
    private Double price;

    @ManyToMany
    @OrderBy("name")
    private List<Category> categories;

    @Basic(optional = false)
    @Column(nullable = false)
    private String Discription;

    @Basic(optional = false)
    @Column(nullable = false)
    private boolean removed;

    public void addCategory(Category category) {
        Objects.requireNonNull(category);
        if (categories == null) {
            this.categories = new ArrayList<>();
        }
        categories.add(category);
    }

    public void removeCategory(Category category) {
        Objects.requireNonNull(category);
        if (categories == null) {
            return;
        }
        categories.removeIf(c -> Objects.equals(c.getId(), category.getId()));
    }

    @Override
    public String toString() {
        return "Product{" +
                "name='" + name + '\'' +
                ", amount=" + amount +
                ", categories=" + categories +
                "}";
    }
}
