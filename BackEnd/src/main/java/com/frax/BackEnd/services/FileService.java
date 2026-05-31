package com.frax.BackEnd.services;


import com.frax.BackEnd.entity.FileEntity;
import com.frax.BackEnd.entity.UserEntity;
import com.frax.BackEnd.repository.FileRepo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileService {
    private final FileRepo fileRepo;

    @Transactional
    public FileEntity uploadFile(MultipartFile file, UserEntity owner) throws IOException {
        // Crea directory se non esiste
        String uploadDir = "uploads/";
        Files.createDirectories(Paths.get(uploadDir));

        // Genera nome univoco per il file
        String originalName = file.getOriginalFilename();
        String extension = "";
        if (originalName != null && originalName.contains(".")) {
            extension = originalName.substring(originalName.lastIndexOf("."));
        }
        String savedName = UUID.randomUUID().toString() + extension;
        Path filePath = Paths.get(uploadDir + savedName);

        // Salva il file fisicamente
        Files.copy(file.getInputStream(), filePath);

        // Crea l'entità
        FileEntity fileEntity = new FileEntity();
        fileEntity.setFileName(originalName);
        fileEntity.setFilePath(filePath.toString());
        fileEntity.setFileSize(file.getSize());
        fileEntity.setFileType(file.getContentType());
        fileEntity.setOwner(owner);
        return fileRepo.save(fileEntity);
    }

    public List<FileEntity> getMyFiles(UserEntity owner) {
        Optional<List<FileEntity>> files = fileRepo.findByOwner(owner);
        return files.orElseThrow(()-> new RuntimeException("Nessun file trovato per: " + owner));
    }

    public FileEntity findById(String fileId) {
        return fileRepo.findById(fileId)
                .orElseThrow(() -> new RuntimeException("File non trovato: " + fileId));
    }

    @Transactional
    public void deleteFile(String fileId, UserEntity owner) {
        FileEntity file = findById(fileId);
        if (!file.getOwner().getId().equals(owner.getId())) {
            throw new RuntimeException("Non sei il proprietario del file");
        }

        // Cancella il file fisico
        try {
            Files.deleteIfExists(Paths.get(file.getFilePath()));
        } catch (IOException e) {
            // Log dell'errore ma continua con l'eliminazione dal DB
        }

        fileRepo.delete(file);
    }
}
