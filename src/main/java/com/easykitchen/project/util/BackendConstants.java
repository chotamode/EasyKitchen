package com.easykitchen.project.util;

import com.easykitchen.project.model.Role;

public class BackendConstants {

    public static final Role DEFAULT_ROLE = Role.USER;
    public static final String AUTH_HEADER_STRING = "Authorization";
    public static final String AUTH_TOKEN_PREFIX = "Bearer ";
    public final static String TOKEN_SECRET = "SECRET_KEYSECRET_KEYSECRET_KEYSECRET_KEYSECRET_KEYSECRET_KEYSECRET_KEYSECRET_KEYSECRET_KEY";
    public final static Integer TOKEN_EXPIRATION_TIME = 86_400_000;

}
