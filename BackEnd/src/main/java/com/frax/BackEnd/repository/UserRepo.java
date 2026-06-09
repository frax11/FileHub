package com.frax.BackEnd.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.frax.BackEnd.entity.UserEntity;

@Repository
public interface UserRepo extends JpaRepository<UserEntity, String> {
    Optional<UserEntity> findByEmail(String email);
    Boolean existsByEmail(String email);

    void deleteByEmail(String email);
}
