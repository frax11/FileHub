package com.frax.BackEnd.controller;


import com.frax.BackEnd.dto.FileDTO;

import com.frax.BackEnd.services.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/files")
public class FileController {
    private final FileService fileService;

    @GetMapping("/get/{id}")
    public ResponseEntity<?> getFile(@PathVariable String id, Authentication authentication) {


        try {
            // Recupera i byte dal service
            byte[] fileBytes = fileService.downloadFile(id, authentication.getName());

            System.out.println("File letto! Dimensione: " + fileBytes.length + " bytes");

            // FONDAMENTALE: Imposta gli Header corretti per dire al browser che è un file scaricabile!
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; id=\"" + id + "\"")
                    .body(fileBytes);

        } catch (Exception e) {
            System.err.println("errore durante il download file: " + e.getMessage());
            return new ResponseEntity<>("Errore: " + e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/get/all")
    public ResponseEntity<?> getAll(Authentication authentication) {
        try{
            List<FileDTO> list = fileService.getAllFiles(authentication.getName());
            return new ResponseEntity<>(list,HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }
    @PostMapping("/delete/{id}")
    public  ResponseEntity<?> deleteFile(@PathVariable String id,Authentication authentication) {
        try{
            fileService.deleteFile(id,authentication.getName());
            return  new ResponseEntity<>(id+" è stato eliminato",HttpStatus.OK);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @PostMapping("/upload")
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file, Authentication authentication) {
        try {
            fileService.uploadFile(file, authentication);
            return new ResponseEntity<>("File caricato", HttpStatus.OK);
        } catch (Exception e) {
            System.err.println("errore durante il upload file: " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
