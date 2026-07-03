package com.frax.BackEnd.services;


import com.frax.BackEnd.dto.FileDTO;
import com.frax.BackEnd.entity.FileEntity;
import com.frax.BackEnd.entity.SharedFile;
import com.frax.BackEnd.entity.UserEntity;
import com.frax.BackEnd.mapper.FileMapper;
import com.frax.BackEnd.mapper.UserMapperImpl;
import com.frax.BackEnd.repository.FileRepo;
import com.frax.BackEnd.repository.SharedFileRepo;
import com.frax.BackEnd.repository.UserRepo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
public class FileService {
    private final FileRepo fileRepo;
    private final UserMapperImpl userMapper;
    private final FileMapper fileMapper;
    private final UserRepo userRepo;
    private final SharedFileRepo sharedFileRepo;

    @Transactional
    public void uploadFile(MultipartFile file, Authentication authentication) throws IOException {


        String UPLOAD_DIR = "uploads/";
        Path path = Path.of(UPLOAD_DIR);
        try {
            Files.createDirectories(path);
        } catch (FileAlreadyExistsException ignore) {}

        String originalName = file.getOriginalFilename();
        if (originalName == null || !originalName.contains(".")) {
            throw new IOException("Formato del file invalido");
        }
        String extension = originalName.substring(originalName.lastIndexOf("."));

        UserEntity owner = userRepo.findByEmail(authentication.getName())
                .orElseThrow(() -> new RuntimeException("Utente non trovato"));

        FileEntity fileEntity = new FileEntity();
        fileEntity.setFileName(originalName);
        fileEntity.setFileSize(file.getSize());
        fileEntity.setFileType(file.getContentType());
        fileEntity.setOwner(owner);
        fileEntity.setFilePath(UPLOAD_DIR);
        fileEntity = fileRepo.save(fileEntity);


        String uniqueFileName = fileEntity.getId() + extension;
        Path finalPath = Path.of(UPLOAD_DIR, uniqueFileName);

        fileEntity.setFilePath(finalPath.toString());
        fileRepo.save(fileEntity);

        Files.copy(file.getInputStream(), finalPath, StandardCopyOption.REPLACE_EXISTING);
    }

    @Transactional
    public List<FileDTO> getAllFiles(String ownerEmail) throws FileNotFoundException {
        List<FileDTO> fileDTOList = new ArrayList<>();
        for ( FileEntity file :fileRepo.findByOwner_Email(ownerEmail).orElseThrow(() -> new FileNotFoundException("File non trovato nel database"))){
            fileDTOList.add(fileMapper.toDTO(file));
        }
        for ( SharedFile file :sharedFileRepo.findBySharedWith_Email(ownerEmail).orElseThrow(() -> new FileNotFoundException("File non trovato nel database"))){
            fileDTOList.add(fileMapper.toDTO(fileRepo.findById(file.getFile().getId()).orElseThrow( () -> new FileNotFoundException("File non trovato nel database"))));
        }
        return fileDTOList;
    }

    @Transactional
    public void deleteFile(String id, String ownerEmail) throws IOException {
        FileEntity file = fileRepo.findFileById(id)
                .orElseThrow(() -> new FileNotFoundException("File non trovato nel database"));
        if (!file.getOwner().getEmail().equals(ownerEmail)) {
            throw new SecurityException("Non sei il proprietario di questo file");
        }
        Path path = Path.of(file.getFilePath());

        if(Files.deleteIfExists(path))
            {
            fileRepo.delete(file);
            }
        else {
            throw new IOException("File non trovato nel database");
        }
    }

    @Transactional
    public byte[] downloadFile(String id, String ownerEmail ) throws IOException {

        FileEntity fileEntity = fileRepo.findFileByIdAndOwner_Email(id, ownerEmail)
                .orElseThrow(() -> new FileNotFoundException("File non trovato nel database"));
        Path filePath = Path.of(fileEntity.getFilePath());
        if (!Files.exists(filePath)) {
            throw new FileNotFoundException("Il file fisico non esiste più sul server");
        }
        return Files.readAllBytes(filePath);
    }

    @Transactional
    public void deleteAllFile(String email) throws IOException {
        List<FileEntity> fileList = fileRepo.findByOwnerEmail(email)
                .orElseThrow(() -> new FileNotFoundException("File non trovato nel database"));
        for(FileEntity file : fileList){
            Path path = Path.of(file.getFilePath());
            if(!Files.deleteIfExists(path))
                throw new IOException("File non trovato nell archivio");

        }



    }

}

