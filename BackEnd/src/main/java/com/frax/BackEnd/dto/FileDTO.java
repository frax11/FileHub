package com.frax.BackEnd.dto;

import com.frax.BackEnd.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class FileDTO {
    private String id;
    private String fileName;
    private String filePath;
    private Long fileSize;
    private String fileType;
    private String ownerEmail;
    private boolean isShared;
    private int currentAccessCount;
    private int  maxAccessCount;
    private LocalDateTime UploadDate;


}
