package com.frax.BackEnd.services;

import com.frax.BackEnd.dto.UserDTO;
import com.frax.BackEnd.dto.UserRegistrationDTO;
import com.frax.BackEnd.entity.UserEntity;
import com.frax.BackEnd.mapper.UserMapper;
import com.frax.BackEnd.repository.UserRepo;
import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    
    private UserMapper userMapper;
    private PasswordEncoder passwordEncoder;
    private UserRepo userRepo;
    
    public UserService(UserRepo userRepo, UserMapper userMapper, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    public UserDTO register(UserRegistrationDTO registrationDTO) {
        
        UserEntity user = userMapper.toEntity(registrationDTO);
        user.setPassword(passwordEncoder.encode(registrationDTO.getPassword()));
        UserEntity savedUser = userRepo.save(user);
        return userMapper.toDTO(savedUser);
    }

    public UserDTO logIn(String email, String password) {
        
        // Trova l'utente per email
        UserEntity user = userRepo.findByEmail(email).orElseThrow(() -> new RuntimeException("Utente o password errati"));
        
        // Verifica la password
        if (passwordEncoder.matches(password, user.getPassword())) {
            return userMapper.toDTO(user);
        } else {
            throw new RuntimeException("Utente o password errati");
        }
    }

    public void deleteUser(String id) {
        userRepo.deleteById(id);
    }

    public List<UserDTO> getAllUsers() {
        List<UserEntity> users = userRepo.findAll();
        return users.stream().map(userMapper::toDTO).collect(java.util.stream.Collectors.toList());
    }


}


