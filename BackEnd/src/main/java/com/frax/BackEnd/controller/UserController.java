package com.frax.BackEnd.controller;

import java.util.Map;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.frax.BackEnd.dto.UserDTO;
import com.frax.BackEnd.dto.UserLoginDTO;
import com.frax.BackEnd.dto.UserRegistrationDTO;
import com.frax.BackEnd.services.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;



@Controller
@RequiredArgsConstructor
@RequestMapping("/auth/users")
public class UserController {
    private final UserService userService;
    private final AuthenticationManager authenticationManager;


    
    @PostMapping("/login")
    public ResponseEntity<?> logIn(@Valid @RequestBody UserLoginDTO loginDTO) {
        // Effettua l'autenticazione e gestisce la sessione
      try {
            System.out.println("Tentativo di login con email: " + loginDTO.getEmail() + " e password: " + loginDTO.getPassword());
            Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(loginDTO.getEmail(), loginDTO.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (Exception e) {
            return ResponseEntity.status(401).body("Login Fallito: " + e.getMessage());
        }
        return ResponseEntity.ok("Login Riuscito");            
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody UserRegistrationDTO userDto) {
          try {
            UserDTO createdUser = userService.register(userDto);
            return ResponseEntity.ok(createdUser);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Errore durante la registrazione: " + e.getMessage());
        }
        
    }

    @GetMapping("/profile")
    public ResponseEntity<?> getUserProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).body("Utente Non Autenticato");
        }
        try {
            Map<String, String> profile = userService.getUserProfile(authentication);
            return ResponseEntity.ok(profile);
        } catch (Exception e) {
            return ResponseEntity.status(404).body("Utente Non Trovato");
        }
        return ResponseEntity.ok("Profilo Utente");
    }


    @PostMapping("/logout")
    public ResponseEntity<?> logOut(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return ResponseEntity.ok("Logout Riuscito");
    }
}