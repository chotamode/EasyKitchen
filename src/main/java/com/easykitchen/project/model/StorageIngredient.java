package com.easykitchen.project.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "INGREDIENTS")
@NamedQueries({
        @NamedQuery(name = "StorageIngredient.findByName", query = "SELECT i FROM StorageIngredient i WHERE i.name = :name")
})

public class StorageIngredient extends AbstractEntity{

    @Basic(optional = false)
    @Column(nullable = false)
    private String name;

    @Basic(optional = false)
    @Column(nullable = false)
    private Integer amount;

    @Basic(optional = false)
    @Column(nullable = false)
    private Unit unit;
}
