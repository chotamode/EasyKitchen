package com.easykitchen.project.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
public class MainPageController {

    @GetMapping("/")
    public String testEndPoint() {
        return "Test API";
    }
}
