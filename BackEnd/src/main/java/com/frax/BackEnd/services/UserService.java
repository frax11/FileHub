package com.frax.BackEnd.services;

import java.util.Optional;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.frax.BackEnd.dto.UserDTO;
import com.frax.BackEnd.dto.UserRegistrationDTO;
import com.frax.BackEnd.entity.UserEntity;
import com.frax.BackEnd.mapper.UserMapper;
import com.frax.BackEnd.repository.UserRepo;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
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

    @Transactional
    public UserDTO saveUser(UserRegistrationDTO user) {
        user.setEmail(user.getEmail().toUpperCase());
        user.setSurname(user.getSurname().toUpperCase());
        user.setName(user.getName().toUpperCase());

        UserEntity userEntity = userMapper.toEntity(user);
        userEntity.setPassword(passwordEncoder.encode(user.getPassword()));
        userEntity.setIsEnabled(true);
        userEntity.setIsAdmin(false);
        userRepo.save(userEntity);
        return userMapper.toDTO(userEntity);
    }

    public void deleteUser(String email) {
        if(userRepo.existsByEmail(email))
            userRepo.deleteByEmail(email);
        else
            throw new UsernameNotFoundException("User not found");
    }

}
