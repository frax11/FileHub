package com.frax.BackEnd.mapper;

import com.frax.BackEnd.dto.UserInfoDTO;
import com.frax.BackEnd.dto.UserLoginDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import com.frax.BackEnd.dto.UserDTO;
import com.frax.BackEnd.dto.UserRegistrationDTO;
import com.frax.BackEnd.entity.UserEntity;

@Mapper(componentModel = "spring")
public interface UserMapper {
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "isAdmin", ignore = true)
    @Mapping(target = "isEnabled", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    UserEntity toEntity(UserRegistrationDTO registrationDTO);

    UserDTO toUserDTO(UserEntity userEntity);

    UserInfoDTO toInfoDTO(UserEntity userEntity);

    
}
