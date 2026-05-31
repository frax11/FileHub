package com.frax.BackEnd.repository;

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

    // Tutte le condivisioni attive per un utente (con file ancora accessibili)
    @Query("SELECT s FROM SharedFile s WHERE s.sharedWith = :user AND s.isActive = true AND s.file.isShared = true AND s.file.currentAccessCount < s.file.maxAccessCount")
    List<SharedFile> findAvailableSharedFilesForUser(@Param("user") UserEntity user);

    // Verifica se un utente ha accesso a un file specifico
    @Query("SELECT CASE WHEN COUNT(s) > 0 THEN true ELSE false END FROM SharedFile s WHERE s.file.id = :fileId AND s.sharedWith = :user AND s.isActive = true AND s.file.currentAccessCount < s.file.maxAccessCount")
    boolean hasUserAccess(@Param("fileId") String fileId, @Param("user") UserEntity user);

    // Trova la condivisione per un file e un utente
    Optional<SharedFile> findByFileIdAndSharedWith(String fileId, UserEntity sharedWith);

    // Tutte le condivisioni per un file
    List<SharedFile> findByFileId(String fileId);

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
}