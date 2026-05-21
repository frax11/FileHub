package com.frax.BackEnd.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import com.frax.BackEnd.dto.UserDTO;
import com.frax.BackEnd.dto.UserRegistrationDTO;
import com.frax.BackEnd.entity.UserEntity;

@Mapper(componentModel = "spring")
public interface UserMapper {
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "isAdmin", ignore = true)
    UserEntity toEntity(UserRegistrationDTO registrationDTO);

    UserDTO toDTO(UserEntity userEntity);


    
}
