package com.easykitchen.project.dao;

import com.easykitchen.project.ProjectApplication;
import com.easykitchen.project.environment.Generator;
import com.easykitchen.project.model.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@DataJpaTest
@ComponentScan(basePackageClasses = ProjectApplication.class)
public class UserDaoTest {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private UserDao sut;

    @Test
    public void findByUsernameReturnsPersonWithMatchingUsername() {
        final User user = Generator.generateUser();
        em.persist(user);

        final User result = sut.findByUsername(user.getUsername());
        assertNotNull(result);
        assertEquals(user.getId(), result.getId());
    }

    @Test
    public void findByUsernameReturnsNullForUnknownUsername() {
        assertNull(sut.findByUsername("unknownUsername"));
    }
}


