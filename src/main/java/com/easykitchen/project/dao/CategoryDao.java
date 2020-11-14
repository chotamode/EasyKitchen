package com.easykitchen.project.dao;

import com.easykitchen.project.model.Category;

import org.springframework.stereotype.Repository;


@Repository
public class CategoryDao extends BaseDao<Category> {
    public CategoryDao() {
        super(Category.class);
    }
}
