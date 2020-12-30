package com.easykitchen.project.rest.auth;

import com.easykitchen.project.model.User;
import com.easykitchen.project.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController("/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<Void> registerUser(@RequestBody User user) {
        userService.createUser(user);
        return new ResponseEntity<>(null, HttpStatus.CREATED);
    }
}
