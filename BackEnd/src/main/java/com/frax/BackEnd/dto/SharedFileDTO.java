package com.frax.BackEnd.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SharedFileDTO {
    private String shareId;
    private String fileId;
    private String fileName;
    private Long fileSize;
    private String fileType;
    private String sharedWithEmail;
    private String sharedWithName;
    private LocalDateTime sharedAt;
    private boolean isActive;
    private int maxAccessCount;      // limite totale del file
    private int currentAccessCount;  // accessi già effettuati (TOTALI)
    private int remainingAccesses;   // accessi rimasti (TOTALI)
    private boolean accessible;      // se è ancora scaricabile
}