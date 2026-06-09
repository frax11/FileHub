package com.frax.BackEnd.mapper;

import com.frax.BackEnd.dto.FileDTO;
import com.frax.BackEnd.entity.FileEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface FileMapper {

    FileEntity FileDTOToFileEntity(FileDTO fileDTO);
    FileDTO FileEntityToFileDTO(FileEntity fileEntity);
}

