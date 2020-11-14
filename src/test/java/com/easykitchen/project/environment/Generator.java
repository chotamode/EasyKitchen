package com.easykitchen.project.environment;

import com.easykitchen.project.model.Recipe;
import com.easykitchen.project.model.User;

import java.util.Random;

public class Generator {
    private static final Random RAND = new Random();

    public static int randomInt() {
        return RAND.nextInt();
    }

    public static boolean randomBoolean() {
        return RAND.nextBoolean();
    }

    public static User generateUser() {
        final User user = new User();
        user.setFirstName("FirstName" + randomInt());
        user.setLastName("LastName" + randomInt());
        user.setUsername("username" + randomInt() + "@kbss.felk.cvut.cz");
        user.setPassword(Integer.toString(randomInt()));
        return user;
    }

    public static Recipe generateRecipe() {
        final Recipe r = new Recipe();
        r.setName("Product" + randomInt());
        r.setAmount(1);
        r.setPrice(1.0);
        return r;
    }
}

