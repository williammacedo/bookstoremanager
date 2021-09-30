package com.williammacedo.bookstoremanager.book.service;

import com.williammacedo.bookstoremanager.author.service.AuthorService;
import com.williammacedo.bookstoremanager.book.builder.BookRequestDTOBuilder;
import com.williammacedo.bookstoremanager.book.builder.BookResponseDTOBuilder;
import com.williammacedo.bookstoremanager.book.dto.BookRequestDTO;
import com.williammacedo.bookstoremanager.book.dto.BookResponseDTO;
import com.williammacedo.bookstoremanager.book.entity.Book;
import com.williammacedo.bookstoremanager.book.mapper.BookMapper;
import com.williammacedo.bookstoremanager.book.repository.BookRepository;
import com.williammacedo.bookstoremanager.publisher.service.PublisherService;
import com.williammacedo.bookstoremanager.user.dto.AuthenticatedUser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    private final BookMapper mapper = BookMapper.INSTANCE;

    @Mock
    AuthorService authorService;
    @Mock
    PublisherService publisherService;
    @Mock
    BookRepository repository;

    @InjectMocks
    BookService service;

    private static BookRequestDTOBuilder bookRequestDTOBuilder;
    private static BookResponseDTOBuilder bookResponseDTOBuilder;
    private static AuthenticatedUser authenticatedUser;

    @BeforeAll
    static void setUp() {
        bookRequestDTOBuilder = BookRequestDTOBuilder.builder().build();
        bookResponseDTOBuilder = BookResponseDTOBuilder.builder().build();
        authenticatedUser = new AuthenticatedUser("william", "123456", "ADMIN");
    }

    @Test
    @DisplayName("Unit test - List all books")
    void whenListBooksIsCalledThenItShouldBeReturned() {
        BookRequestDTO expectedBookRequestDTO = bookRequestDTOBuilder.buildBookRequestDTO();
        BookResponseDTO expectedBookResponseDTO = bookResponseDTOBuilder.buildBookResponseDTO();
        Book book = mapper.toModel(expectedBookRequestDTO);
        book.setId(1L);

        when(repository.getBooksAuthorsAndPublishers()).thenReturn(Collections.singletonList(book));

        List<BookResponseDTO> bookResponseDTOS = service.findAll();

        Assertions.assertFalse(bookResponseDTOS.isEmpty());
        Assertions.assertEquals(expectedBookResponseDTO, bookResponseDTOS.get(0));

        verify(repository, times(1)).getBooksAuthorsAndPublishers();
    }
}
