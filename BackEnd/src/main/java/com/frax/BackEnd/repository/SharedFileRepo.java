package com.frax.BackEnd.repository;

import com.frax.BackEnd.entity.FileEntity;
import com.frax.BackEnd.entity.SharedFile;
import com.frax.BackEnd.entity.UserEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;


public interface SharedFileRepo extends JpaRepository<SharedFile, Long> {


    // Verifica se un utente ha accesso a un file specifico
    @Query("SELECT CASE WHEN COUNT(s) > 0 THEN true ELSE false END FROM SharedFile s WHERE s.file.id = :fileId AND s.sharedWith = :user AND s.isActive = true AND s.file.currentAccessCount < s.file.maxAccessCount")
    boolean hasUserAccess(@Param("fileId") String fileId, @Param("user") UserEntity user);

    // Verifica se esiste già una condivisione
    boolean existsByFileIdAndSharedWith(String fileId, UserEntity sharedWith);

    // Disattiva tutte le condivisioni per un file
    @Modifying
    @Transactional
    @Query("UPDATE SharedFile s SET s.isActive = false WHERE s.file.id = :fileId")
    void deactivateAllSharesForFile(@Param("fileId") String fileId);

    // Disattiva la condivisione per un singolo utente
    @Modifying
    @Transactional
    @Query("UPDATE SharedFile s SET s.isActive = false WHERE s.file.id = :fileId AND s.sharedWith = :user")
    void deactivateShareForUser(@Param("fileId") String fileId, @Param("user") UserEntity user);


    Optional<List<SharedFile>> findBySharedWith_Email(String sharedWith);

    @Modifying
    @Transactional
    void deleteByFile(FileEntity file);

    Optional<SharedFile> findByFile_IdAndSharedWith_Email(String id, String sharedWith_email);
}