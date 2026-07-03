package com.frax.BackEnd.repository;

import com.frax.BackEnd.entity.FileEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FileRepo extends JpaRepository<FileEntity, String> {
    @Modifying
    @Transactional
    @Query("UPDATE FileEntity f " +
            "SET f.currentAccessCount = f.currentAccessCount + 1 " +
            "WHERE f.id = :fileId " +
            "AND f.currentAccessCount+1 < f.maxAccessCount")
    int incrementGlobalAccessCount(@Param("fileId") String fileId);

    @Query("SELECT f FROM FileEntity f "
            +"JOIN f.owner u "
            +"WHERE u.email = :email")
    Optional<List<FileEntity>> findByOwnerEmail(
            @Param("email") String email);

    Optional<FileEntity> findFileByIdAndOwner_Email(String id, String ownerEmail);

    Optional<List<FileEntity>> findByOwner_Email(String ownerEmail);

    Optional<FileEntity> findFileById(String id);

}
