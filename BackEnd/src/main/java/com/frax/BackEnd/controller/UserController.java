package com.frax.BackEnd.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    @GetMapping("/profile")
    public String getProfile() {
        return "User profile";
    }

    @PostMapping("/logout")
    public String logout() {
        return "User logged out";
    }
}
