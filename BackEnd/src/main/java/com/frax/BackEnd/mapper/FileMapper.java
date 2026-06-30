package com.frax.BackEnd.mapper;

import com.frax.BackEnd.dto.FileDTO;
import com.frax.BackEnd.entity.FileEntity;
import com.frax.BackEnd.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface FileMapper {

    FileEntity toEntity(FileDTO fileDTO);
    @Mapping(target = "ownerEmail",source = "owner",qualifiedByName = "getOwnerEmail")
    FileDTO toDTO(FileEntity fileEntity);

    @Named("getOwnerEmail")
    default String getOwnerEmail(UserEntity owner) {
        return owner != null ? owner.getEmail() : null;
    }
}

