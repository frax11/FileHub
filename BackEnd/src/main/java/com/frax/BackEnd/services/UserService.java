package com.frax.BackEnd.services;

import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

import com.frax.BackEnd.dto.UserInfoDTO;
import com.frax.BackEnd.dto.UserUpdateDTO;
import com.frax.BackEnd.repository.FileRepo;
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
    private final FileRepo fileRepo;
    private final FileService fileService;

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
        return userMapper.toUserDTO(userEntity);
    }
    @Transactional
    public void deleteUser(String email) throws IOException {
        if(userRepo.existsByEmail(email)) {
            fileService.deleteAllFile(email);
            userRepo.deleteByEmail(email);
        } else{
            throw new UsernameNotFoundException("User not found");
        }
    }
    @Transactional
    public void updateUser(UserUpdateDTO userUpdateDTO, String email) {
        UserEntity user = userRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Utente non trovato"));
        if (passwordEncoder.matches(userUpdateDTO.getCurrentPassword(), user.getPassword())) {
            if (userUpdateDTO.getName() != null && !userUpdateDTO.getName().isBlank()) {
                user.setName(userUpdateDTO.getName().toUpperCase());
            }
            if (userUpdateDTO.getSurname() != null && !userUpdateDTO.getSurname().isBlank()) {
                user.setSurname(userUpdateDTO.getSurname().toUpperCase());
            }
            if (userUpdateDTO.getUpdatePassword() != null && !userUpdateDTO.getUpdatePassword().isBlank()) {
                user.setPassword(passwordEncoder.encode(userUpdateDTO.getUpdatePassword()));
            }
            userRepo.save(user);
        }else {
            throw new SecurityException("Password Errata");
        }

    }

    public UserInfoDTO getUser(String  email) {
        return userMapper.toInfoDTO(userRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Utente non trovato")));
    }
}
