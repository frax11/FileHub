package com.frax.BackEnd.services;

import com.frax.BackEnd.dto.SharedRequestDTO;
import com.frax.BackEnd.entity.FileEntity;
import com.frax.BackEnd.entity.SharedFile;
import com.frax.BackEnd.entity.UserEntity;
import com.frax.BackEnd.repository.FileRepo;
import com.frax.BackEnd.repository.SharedFileRepo;
import com.frax.BackEnd.repository.UserRepo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@RequiredArgsConstructor
public class ShareService {
    private final FileRepo fileRepo;
    private final UserRepo userRepo;
    private final SharedFileRepo sharedFileRepo;

    @Transactional
    public ResponseEntity<?> shareFileWithUsers(SharedRequestDTO request, String ownerEmail) {

        String shareEmail = request.getShareTo().toUpperCase();
        UserEntity owner = userRepo.findByEmail(ownerEmail)
                .orElseThrow(() -> new RuntimeException("Utente non trovato: " + ownerEmail));
        FileEntity file = fileRepo.findById(request.getId())
                .orElseThrow(() -> new RuntimeException("File non trovato"));

        if (!file.getOwner().getId().equals(owner.getId())) {
            throw new RuntimeException("Non sei il proprietario del file");
        }
        file.setShared(true);
        file.setMaxAccessCount(request.getMaxAccessCount());
        file.setCurrentAccessCount(0);
        fileRepo.save(file);


        UserEntity user = userRepo.findByEmail(shareEmail)
                .orElseThrow(() -> new RuntimeException("Utente non trovato: " + shareEmail));

        if (!sharedFileRepo.existsByFileIdAndSharedWith(file.getId(), user)) {
            SharedFile sharedFile = new SharedFile();
            sharedFile.setFile(file);
            sharedFile.setSharedWith(user);
            sharedFileRepo.save(sharedFile);
        }

        return ResponseEntity.ok().build();

    }

    @Transactional

    public FileEntity getSharedFile(String fileId, String userEmail) {
        UserEntity user = userRepo
                .findByEmail(userEmail)
                .orElseThrow(() -> new SecurityException("Utente non trovato: " + userEmail));

        int updated = fileRepo.incrementGlobalAccessCount(fileId);
        if (updated == 0) {
            SharedFile sharedFile = sharedFileRepo
                    .findByFile_IdAndSharedWith_Email(fileId, userEmail)
                    .orElseThrow(() -> new RuntimeException("Nessuna condivisione trovata per questo file"));

            if (user.getSharedWithMe() != null) {
                user.getSharedWithMe().remove(sharedFile);
                sharedFileRepo.delete(sharedFile);
            }
        }
        return fileRepo.findById(fileId).orElseThrow(() -> new RuntimeException("File non trovato"));
    }


    @Transactional
    public void revokeAccess(String fileId, String email) {
        SharedFile sharedFile = sharedFileRepo.findByFile_IdAndSharedWith_Email(fileId, email)
                .orElseThrow(() -> new RuntimeException("Nessuna condivisione trovata per questo file"));

        sharedFileRepo.delete(sharedFile);
    }
}

