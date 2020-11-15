package com.easykitchen.project.dao;

import com.easykitchen.project.model.Item;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Objects;


public class ItemDao {

    @PersistenceContext
    private EntityManager em;

    public Item find(Integer id) {
        Objects.requireNonNull(id);
        return em.find(Item.class, id);
    }
}
