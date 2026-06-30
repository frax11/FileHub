package com.frax.BackEnd.controller;

import com.frax.BackEnd.dto.SharedRequestDTO;
import com.frax.BackEnd.services.ShareService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/share")
public class ShareController {
    private final ShareService shareService;

    @PostMapping("/add")
    public ResponseEntity<?> shareFile(@RequestBody SharedRequestDTO request, Authentication authentication) {

        try {
            return shareService.shareFileWithUsers(request, authentication.getName());
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}