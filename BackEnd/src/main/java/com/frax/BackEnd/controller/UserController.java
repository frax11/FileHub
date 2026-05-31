package com.frax.BackEnd.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {


    @GetMapping("/me")
    public ResponseEntity<?> getMe(Authentication authentication, HttpServletRequest request) {
        System.out.println("=== GET /user/me ===");
        System.out.println("Authentication: " + authentication);
        System.out.println("Session ID: " + request.getSession().getId());

        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).body("Utente non autenticato");
        }
        return ResponseEntity.ok(authentication.getPrincipal());
    }

}
    