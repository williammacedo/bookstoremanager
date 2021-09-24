package com.williammacedo.bookstoremanager.book.mapper;

import com.williammacedo.bookstoremanager.author.mapper.AuthorMapper;
import com.williammacedo.bookstoremanager.book.dto.BookRequestDTO;
import com.williammacedo.bookstoremanager.book.dto.BookResponseDTO;
import com.williammacedo.bookstoremanager.book.entity.Book;
import com.williammacedo.bookstoremanager.publisher.mapper.PublisherMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(uses = {AuthorMapper.class, PublisherMapper.class})
public interface BookMapper {

    BookMapper INSTANCE = Mappers.getMapper(BookMapper.class);

    BookResponseDTO toDTO(Book entity);

    List<BookResponseDTO> booksToBooksDtos(List<Book> cars);

    @Mappings({
        @Mapping(source = "authorId", target = "author.id"),
        @Mapping(source = "publisherId", target = "publisher.id")
    })
    Book toModel(BookRequestDTO dto);

    Book updateUser(@MappingTarget Book entity, BookRequestDTO dto);
}
