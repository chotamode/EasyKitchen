package com.easykitchen.project.dao;

import org.springframework.stereotype.Repository;
import com.easykitchen.project.model.User;

import javax.persistence.NoResultException;
import java.util.Objects;

@Repository
public class UserDao extends BaseDao<User> {

    public UserDao() {
        super(User.class);
    }

    public User findByUsername(String username) {
        try {
            return em.createNamedQuery("User.findByUsername", User.class).setParameter("username", username)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
}
