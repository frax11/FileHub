package com.frax.BackEnd.controller;

import java.util.List;

import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.frax.BackEnd.dto.UserDTO;
import com.frax.BackEnd.dto.UserRegistrationDTO;
import com.frax.BackEnd.services.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@Controller
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }
    
    @GetMapping("/login{email}/{password}")
    public ResponseEntity<UserDTO> logIn(@PathVariable String email, @PathVariable String password){
        return ResponseEntity.ok(userService.logIn(email, password));
    }

    @PostMapping("/add")
    public ResponseEntity<?> addUser(@RequestBody UserRegistrationDTO userDto) {
          try {
            UserDTO createdUser = userService.register(userDto);
            return ResponseEntity.ok(createdUser);
        } catch (Exception e) {
            e.getMessage();
            return ResponseEntity.badRequest().body(e.getMessage()); 
        }
        
    }
    
}
