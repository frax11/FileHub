package com.frax.BackEnd.dto;

import com.frax.BackEnd.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class FileDTO {
    private String id;
    private String fileName;
    private String filePath;
    private Long fileSize;
    private String fileType;
    private UserEntity owner;
    private boolean isShared;
    private int maxAccessCount ;
    private int currentAccessCount ;

}
