package com.frax.BackEnd.repository;

import com.frax.BackEnd.entity.FileEntity;
import com.frax.BackEnd.entity.UserEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.io.File;
import java.util.List;
import java.util.Optional;

public interface FileRepo extends JpaRepository<FileEntity, String> {
    Optional<List<FileEntity>> findByOwner(UserEntity owner);


    // Incrementa il contatore globale (operazione atomica)
    @Modifying
    @Transactional
    @Query("UPDATE FileEntity f SET f.currentAccessCount = f.currentAccessCount + 1 WHERE f.id = :fileId AND f.currentAccessCount < f.maxAccessCount")
    int incrementGlobalAccessCount(@Param("fileId") String fileId);


}
