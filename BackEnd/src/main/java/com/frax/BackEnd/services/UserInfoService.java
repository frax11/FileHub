package com.frax.BackEnd.services;

import com.frax.BackEnd.controller.AuthController;
import java.util.List;
import java.util.Optional;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.frax.BackEnd.dto.UserDTO;
import com.frax.BackEnd.dto.UserLoginDTO;
import com.frax.BackEnd.dto.UserRegistrationDTO;
import com.frax.BackEnd.entity.UserEntity;
import com.frax.BackEnd.mapper.UserMapper;
import com.frax.BackEnd.repository.UserRepo;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserInfoService implements UserDetailsService {
    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<UserEntity> user = userRepo.findByEmail(email);
        if (user.isEmpty()) {
            throw new UsernameNotFoundException("Utente non trovato con email: " + email);
        }
            return  User.builder()
                    .username(user.get().getEmail())
                    .password(user.get().getPassword())
                    .authorities(user.get().getIsAdmin() ? "ROLE_ADMIN" : "ROLE_USER")
                    .disabled(!user.get().getIsEnabled())
                    .accountLocked(!user.get().getIsEnabled())
                    .build();
            
    }

    public UserDTO saveUser(UserRegistrationDTO user) {
        user.setEmail(user.getEmail().toLowerCase());
        user.setSurname(user.getSurname().toLowerCase());
        user.setName(user.getSurname().toLowerCase());

        UserEntity userEntity = userMapper.toEntity(user);
        userEntity.setPassword(passwordEncoder.encode(user.getPassword()));
        userEntity.setIsEnabled(true);
        userEntity.setIsAdmin(false);
        userRepo.save(userEntity);
        return userMapper.toDTO(userEntity);
    }

    public UserDTO login(UserLoginDTO loginDTO) throws Exception {
        Optional<UserEntity> userOpt = userRepo.findByEmail(loginDTO.getEmail());
        if (userOpt.isEmpty()) {
            throw new Exception("Utente non trovato con email: " + loginDTO.getEmail());
        }
        UserEntity user = userOpt.get();
        if (!passwordEncoder.matches(loginDTO.getPassword(), user.getPassword())) {
            throw new Exception("Password errata");
        }
        return userMapper.toDTO(user);        
    }
    
}
