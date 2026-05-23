package com.frax.BackEnd.services;

import com.frax.BackEnd.dto.UserDTO;
import com.frax.BackEnd.dto.UserRegistrationDTO;
import com.frax.BackEnd.entity.UserEntity;
import com.frax.BackEnd.mapper.UserMapper;
import com.frax.BackEnd.repository.UserRepo;

import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Map;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final UserRepo userRepo;
    

    public UserDTO register(UserRegistrationDTO registrationDTO)  {
        
        UserEntity user = userMapper.toEntity(registrationDTO);
        user.setPassword(passwordEncoder.encode(registrationDTO.getPassword()));
        UserEntity savedUser = userRepo.save(user);
        return userMapper.toDTO(savedUser);
    }

    public Map<String , String> getUserProfile(Authentication authentication) {
        String email = authentication.getName();
        UserEntity user = userRepo.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Utente Non Trovato: " + email));
        
        return Map.of(
            "name", user.getName(),
            "surname", user.getSurname(),
            "email", user.getEmail(),
            "isAdmin", user.getIsAdmin().toString()
        );
    }
    public void deleteUser(String id) {
        userRepo.deleteById(id);
    }
    
    // Metodo per ottenere tutti gli utenti
    public List<UserDTO> getAllUsers() {
        List<UserEntity> users = userRepo.findAll();
        return users.stream().map(userMapper::toDTO).collect(java.util.stream.Collectors.toList());
    }

   
}


