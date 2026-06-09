package com.frax.BackEnd.repository;

import com.frax.BackEnd.dto.FileDTO;
import com.frax.BackEnd.entity.FileEntity;
import com.frax.BackEnd.entity.UserEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FileRepo extends JpaRepository<FileEntity, String> {
    // Incrementa il contatore globale (operazione atomica)
    @Modifying
    @Transactional
    @Query("UPDATE FileEntity f " +
            "SET f.currentAccessCount = f.currentAccessCount + 1 " +
            "WHERE f.id = :fileId " +
            "AND f.currentAccessCount < f.maxAccessCount")
    int incrementGlobalAccessCount(@Param("fileId") String fileId);

    @Query("SELECT s.file FROM SharedFile s " +
            "JOIN s.sharedWith u " +
            "WHERE u.email = :email")
    Optional<List<FileEntity>> findSharedFilesByUserEmail(
            @Param("email") String email);

    @Query("SELECT f.id from FileEntity f "
            +"JOIN f.owner u "
            +"WHERE f.fileName = :fileName "
            +"AND u.email = :email "
            )
    Optional<String> getIdByName(
            @Param("fileName") String fileName,
            @Param("email") String email);

    @Query("SELECT f  FROM FileEntity f "+
    "JOIN f.owner u "+
    "WHERE u.email =:email "+
    "AND f.fileName = :fileName "
    )
    Optional<FileEntity> findFileByFileName(
            @Param("fileName") String fileName,
            @Param("email") String email);

    @Query("SELECT f FROM FileEntity f "
            +"JOIN f.owner u "
            +"WHERE u.email = :email")
    Optional<List<FileEntity>> findByOwnerEmail(
            @Param("email") String email);
}
