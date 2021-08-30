package com.williammacedo.bookstoremanager.author.mapper;

import com.williammacedo.bookstoremanager.author.dto.AuthorDTO;
import com.williammacedo.bookstoremanager.author.entity.Author;
import org.mapstruct.factory.Mappers;

public interface AuthorMapper {

    AuthorMapper INSTANCE = Mappers.getMapper(AuthorMapper.class);

    Author toModel(AuthorDTO dto);
    AuthorDTO toDTO(Author entity);
}
