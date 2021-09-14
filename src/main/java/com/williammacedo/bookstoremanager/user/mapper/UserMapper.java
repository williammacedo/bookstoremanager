package com.williammacedo.bookstoremanager.user.mapper;

import com.williammacedo.bookstoremanager.user.dto.UserDTO;
import com.williammacedo.bookstoremanager.user.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    UserDTO toDTO(User entity);

    User toModel(UserDTO dto);

    @Mapping(target = "id", ignore = true)
    User updateUser(@MappingTarget User entity, UserDTO dto);
}
