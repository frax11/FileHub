package com.frax.BackEnd.repository;

import com.frax.BackEnd.entity.SharedFile;
import com.frax.BackEnd.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface SharedFileRepo extends JpaRepository<SharedFile, Long> {


    boolean existsByFileIdAndSharedWith(String fileId, UserEntity sharedWith);

    Optional<List<SharedFile>> findBySharedWith_Email(String sharedWith);

    Optional<SharedFile> findByFile_IdAndSharedWith_Email(String id, String sharedWith_email);
}