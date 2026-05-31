package com.frax.BackEnd.services;


import com.frax.BackEnd.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {


    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;



    


}
