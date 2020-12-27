package com.easykitchen.project.security;

import com.easykitchen.project.model.User;
import com.easykitchen.project.util.BackendConstants;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;


public class UsernameAndPasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authManager;

    public UsernameAndPasswordAuthenticationFilter(AuthenticationManager authManager) {
        this.authManager = authManager;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {
        try {
            User credentials = new ObjectMapper()
                    .readValue(request.getInputStream(), User.class);
            Authentication auth = new UsernamePasswordAuthenticationToken(
                    credentials.getUsername(),
                    credentials.getPassword(),
                    new ArrayList<>()
            );
            return authManager.authenticate(auth);
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) {
        String token = Jwts.builder()
                .setSubject(authResult.getName())
                .claim("authorities", authResult.getAuthorities())
                .setIssuedAt(new Date())
                .setExpiration(getTokenExpirationDate())
                .signWith(Keys.hmacShaKeyFor(BackendConstants.TOKEN_SECRET.getBytes()))
                .compact();

        response.addHeader(BackendConstants.AUTH_HEADER_STRING, BackendConstants.AUTH_TOKEN_PREFIX + token);
    }

    private Date getTokenExpirationDate() {
        return new Date(System.currentTimeMillis() + BackendConstants.TOKEN_EXPIRATION_TIME);
    }
}
