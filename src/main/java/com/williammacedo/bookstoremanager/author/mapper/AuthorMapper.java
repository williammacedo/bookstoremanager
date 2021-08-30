package com.williammacedo.bookstoremanager.author.mapper;

import com.williammacedo.bookstoremanager.author.dto.AuthorDTO;
import com.williammacedo.bookstoremanager.author.entity.Author;
import org.mapstruct.Mapper;

@Mapper(componentModel="spring")
public interface AuthorMapper {

    Author toModel(AuthorDTO dto);
    AuthorDTO toDTO(Author entity);
}
