package com.frax.BackEnd.controller;


import com.frax.BackEnd.dto.FileDTO;

import com.frax.BackEnd.services.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/file")
public class FileController {
    private final FileService fileService;

    @GetMapping("/get/{fileName}")
    public ResponseEntity <?> getFile(@PathVariable String fileName,Authentication authentication) {
        try{
            return new ResponseEntity<>(fileService.downloadFile(fileName,authentication.getName()), HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/get/all")
    public ResponseEntity<?> getAll(Authentication authentication) {
        try{
            List<FileDTO> list = fileService.getAllFiles(authentication.getName());
            return new ResponseEntity<>(false,HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }
    @PostMapping("/delete/{fileName}")
    public  ResponseEntity<?> deleteFile(@PathVariable String fileName,Authentication authentication) {
        try{
            fileService.deleteFile(fileName,authentication.getName());
            return  new ResponseEntity<>(fileName+" è stato eliminato",HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}
