package com.frax.BackEnd.services;

import com.frax.BackEnd.dto.SharedFileDTO;
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
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@RequestMapping("/share")
public class ShareService {
    private final FileRepo fileRepo;
    private final UserRepo userRepo;
    private final SharedFileRepo sharedFileRepo;

    @Transactional
    public ResponseEntity<?> shareFileWithUsers(SharedRequestDTO request, String ownerEmail) {

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

        for (String email : request.getUserEmails()) {

            UserEntity user = userRepo.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("Utente non trovato: " + email));

            if (!sharedFileRepo.existsByFileIdAndSharedWith(file.getId(), user)) {
                SharedFile sharedFile = new SharedFile();
                sharedFile.setFile(file);
                sharedFile.setSharedWith(user);
                sharedFileRepo.save(sharedFile);
            }
        }
        return ResponseEntity.ok().build();

    }

    @Transactional
    public FileEntity accessSharedFile(String fileId, String userEmail) {
        UserEntity user = userRepo.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("Utente non trovato: " + userEmail));

        FileEntity file = fileRepo.findById(fileId)
                .orElseThrow(() -> new RuntimeException("File non trovato"));

        // Verifica che l'utente abbia accesso
        boolean hasAccess = sharedFileRepo.hasUserAccess(fileId, user)
                || file.getOwner().getId().equals(user.getId());

        if (!hasAccess) {
            throw new RuntimeException("Non hai accesso a questo file");
        }

        // Verifica che ci siano ancora accessi GLOBALI disponibili
        if (file.getCurrentAccessCount() >= file.getMaxAccessCount()) {
            throw new RuntimeException("Limite di accessi esaurito (" + file.getMaxAccessCount() + " letture massime totali)");
        }

        // INCREMENTA IL CONTATORE GLOBALE (operazione atomica)
        int updated = fileRepo.incrementGlobalAccessCount(fileId);
        if (updated == 0) {
            throw new RuntimeException("Impossibile accedere: limite raggiunto");
        }

        return fileRepo.findById(fileId).orElseThrow();
    }
}

