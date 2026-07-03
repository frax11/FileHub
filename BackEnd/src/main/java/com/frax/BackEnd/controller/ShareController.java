package com.frax.BackEnd.controller;

import com.frax.BackEnd.dto.SharedRequestDTO;
import com.frax.BackEnd.entity.FileEntity;
import com.frax.BackEnd.services.FileService;
import com.frax.BackEnd.services.ShareService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/shared")
public class ShareController {
    private final FileService fileService;
    private final ShareService shareService;

    @PostMapping("/add")
    public ResponseEntity<?> shareFile(@RequestBody SharedRequestDTO request, Authentication authentication) {

        try {
            if (request.getShareTo().toUpperCase().equals(authentication.getName()))
                return ResponseEntity.badRequest().body("Non si puo condividere un file con se stesso");

            return shareService.shareFileWithUsers(request, authentication.getName());
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("get/{id}")
    public ResponseEntity<?> getSharedFile(@PathVariable String id, Authentication authentication) {
        try {
            FileEntity f = shareService.getSharedFile(id, authentication.getName());
            byte[] fileBytes = fileService.downloadFile(id, f.getOwner().getEmail());
            System.out.println("File letto! Dimensione: " + fileBytes.length + " bytes");

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; id=\"" + id + "\"")
                    .body(fileBytes);

        } catch (Exception e) {
            System.err.println("errore durante il download file: " + e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }


    @DeleteMapping("/revoke/{fileId}")
    public ResponseEntity<?> revokeMyAccess(@PathVariable String fileId, Authentication authentication) {
        try {
            shareService.revokeAccess(fileId, authentication.getName());
            return ResponseEntity.ok().body("{\"message\": \"Accesso revocato con successo\"}");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("{\"error\": \"" + e.getMessage() + "\"}");
        }
    }
}