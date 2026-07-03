package com.frax.BackEnd.controller;

import com.frax.BackEnd.dto.UserDTO;
import com.frax.BackEnd.dto.UserLoginDTO;
import com.frax.BackEnd.dto.UserRegistrationDTO;
import com.frax.BackEnd.dto.UserUpdateDTO;
import com.frax.BackEnd.entity.UserEntity;
import com.frax.BackEnd.mapper.UserMapper;
import com.frax.BackEnd.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final UserMapper userMapper;

    @PostMapping("/login")
    public ResponseEntity<?> logIn(@Valid @RequestBody UserLoginDTO loginDTO,HttpServletRequest request, HttpServletResponse response) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    loginDTO.getEmail(),
                    loginDTO.getPassword()
                )
            );
            SecurityContext context = SecurityContextHolder.createEmptyContext();
            context.setAuthentication(authentication);
            SecurityContextHolder.setContext(context);

            HttpSessionSecurityContextRepository repository = new HttpSessionSecurityContextRepository();
            repository.saveContext(context, request, response);


            return ResponseEntity.ok("Login effettuato");
        } catch (Exception e) {
            return ResponseEntity.status(401).body(
                "email o password non trovati"
            );
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody UserRegistrationDTO userDTO) {
        try {
            UserDTO createdUser = userService.saveUser(userDTO);
            return ResponseEntity.ok(createdUser);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                "Errore nella registrazione"
            );
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(Authentication authentication, HttpServletRequest request, HttpServletResponse response) {
        new SecurityContextLogoutHandler().logout(
            request,
            response,
            authentication
        );
        return ResponseEntity.ok("Logout effettuato");
    }

    @PostMapping("/delete")
    public ResponseEntity<?> deleteUser(Authentication authentication, HttpServletRequest request, HttpServletResponse response) {
        try {
            userService.deleteUser(authentication.getName());
            logout(authentication, request, response);
        } catch (Exception e) {
            return ResponseEntity.status(401).body(e);
        }

        return ResponseEntity.ok("User deleted");
    }

    @GetMapping("/me")
    public ResponseEntity<?> getMe(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).body("Utente non autenticato");
        }
        return ResponseEntity.ok(userService.getUser(authentication.getName()));
    }

    @PostMapping("/update")
    public ResponseEntity<?> updateProfile(@RequestBody UserUpdateDTO userUpdateDTO, Authentication authentication) {
        try {
            userService.updateUser(userUpdateDTO,authentication.getName());
            return ResponseEntity.ok().body("{\"message\": \"Profilo aggiornato con successo\"}");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("{\"error\": \"Errore aggiornamento: " + e.getMessage() + "\"}");
        }
    }

}
