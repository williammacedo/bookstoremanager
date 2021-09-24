package com.williammacedo.bookstoremanager.book.service;

import com.williammacedo.bookstoremanager.author.service.AuthorService;
import com.williammacedo.bookstoremanager.book.dto.BookResponseDTO;
import com.williammacedo.bookstoremanager.book.mapper.BookMapper;
import com.williammacedo.bookstoremanager.book.repository.BookRepository;
import com.williammacedo.bookstoremanager.publisher.service.PublisherService;
import com.williammacedo.bookstoremanager.user.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class BookService {

    private final BookMapper mapper = BookMapper.INSTANCE;

    private UserService userService;
    private AuthorService authorService;
    private PublisherService publisherService;
    private BookRepository repository;

    public List<BookResponseDTO> findAll() {
        return mapper.booksToBooksDtos(repository.findAll());
    }
}
