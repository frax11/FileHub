package com.frax.BackEnd.services;


import com.frax.BackEnd.dto.FileDTO;
import com.frax.BackEnd.dto.UserLoginDTO;
import com.frax.BackEnd.entity.FileEntity;
import com.frax.BackEnd.entity.UserEntity;
import com.frax.BackEnd.mapper.FileMapper;
import com.frax.BackEnd.mapper.UserMapperImpl;
import com.frax.BackEnd.repository.FileRepo;
import com.frax.BackEnd.repository.UserRepo;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.Email;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class FileService {
    private final FileRepo fileRepo;
    private final UserMapperImpl userMapper;
    private final FileMapper fileMapper;
    private final UserRepo userRepo;

    @Transactional
    public void uploadFile(MultipartFile file,Authentication authentication) throws IOException {

        // Crea directory se non esiste
        String uploadDir ="uploads/";
        Path path = Path.of(uploadDir);
        try {
            Files.createDirectories(path);
        } catch (FileAlreadyExistsException ignore) {}

        // Genera nome univoco per il file
        String originalName = file.getOriginalFilename();
        String extension = "";
        if (originalName == null || !originalName.contains(".")) {
            throw new IOException("Formato del file invalido");
        }
        extension = originalName.substring(originalName.lastIndexOf("."));

        // Crea l'entità
        FileEntity fileEntity = new FileEntity();
        fileEntity.setFileName(originalName);
        fileEntity.setFilePath(path.toString());
        fileEntity.setFileSize(file.getSize());
        fileEntity.setFileType(file.getContentType());
        UserEntity owner = userRepo.findByEmail(authentication.getName()).orElseThrow();
        fileEntity.setOwner(owner);
        fileRepo.save(fileEntity);

        // Salva il file nel server con nome univoco
        Files.copy(file.getInputStream(),Path.of(uploadDir+fileRepo.getIdByName(originalName, owner.getEmail()).orElseThrow()), StandardCopyOption.REPLACE_EXISTING);
    }

    //Scarica tutti i file
    @Transactional
    public List<Resource> downloadAllFiles(String email) throws IOException {
        Optional<List<FileEntity>> files = fileRepo.findByOwnerEmail(email);
        List<Resource> resourcesList = new ArrayList<>();
        for (FileEntity file : files.orElseThrow()) {
            Path path = Path.of(file.getFilePath()+fileRepo.getIdByName(file.getFileName(),email).orElseThrow());
            Resource resource = new FileSystemResource(path.toFile());
            if (!resource.exists()) {
                throw new FileNotFoundException("File non trovato");
            }
            resourcesList.add(resource);
        }
        return resourcesList;
    }
    //Prende le informazioni dei file
    @Transactional
    public List<FileDTO> getAllFiles(String email) {
        List<FileDTO> fileDTOList = new ArrayList<>();
        for ( FileEntity file :  fileRepo.findSharedFilesByUserEmail(email).orElseThrow() ){
            fileDTOList.add(fileMapper.FileEntityToFileDTO(file));
        }
        return fileDTOList;
    }

    @Transactional
    public void deleteFile(String fileName, String email) throws IOException {
        FileEntity file = fileRepo.findFileByFileName(fileName,email).orElseThrow();
        String id = file.getId();
        String uploadDir = file.getFilePath();
        Path path = Path.of(uploadDir+id);
        Files.deleteIfExists(path);
    }

    @Transactional
    public Resource downloadFile(String fileName,String email ){

        FileEntity file = fileRepo.findFileByFileName(fileName,email).orElseThrow();
        String id = file.getId();
        String uploadDir = file.getFilePath();
        Path path = Path.of(uploadDir+id);
        return new FileSystemResource(path.toFile());
    }




}
