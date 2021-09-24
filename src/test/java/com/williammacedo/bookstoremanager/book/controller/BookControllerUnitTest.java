package com.williammacedo.bookstoremanager.book.controller;

import com.williammacedo.bookstoremanager.book.builder.BookRequestDTOBuilder;
import com.williammacedo.bookstoremanager.book.builder.BookResponseDTOBuilder;
import com.williammacedo.bookstoremanager.book.dto.BookResponseDTO;
import com.williammacedo.bookstoremanager.book.service.BookService;
import com.williammacedo.bookstoremanager.exception.BookstoreExceptionHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import java.util.Collections;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class BookControllerUnitTest {

    private static final String BOOKS_API_URL_PATH = "/v1/books";

    @Mock
    BookService service;

    @InjectMocks
    BookController controller;

    @Autowired
    MockMvc mockMvc;

    BookRequestDTOBuilder bookRequestDTOBuilder;
    BookResponseDTOBuilder bookResponseDTOBuilder;

    @BeforeEach
    void setUp() {
        bookRequestDTOBuilder = BookRequestDTOBuilder.builder().build();
        bookResponseDTOBuilder = BookResponseDTOBuilder.builder().build();
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(BookstoreExceptionHandler.class)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .setViewResolvers((s, locale) -> new MappingJackson2JsonView())
                .build();
    }

    @Test
    @DisplayName("Controller UT - Find all books")
    void whenGETIsCalledThenShouldReturnListOfBooks() throws Exception {
        BookResponseDTO expectedBookResponseDTO = bookResponseDTOBuilder.buildBookResponseDTO();

        when(service.findAll()).thenReturn(Collections.singletonList(expectedBookResponseDTO));

        mockMvc.perform(MockMvcRequestBuilders.get(BOOKS_API_URL_PATH))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(equalTo(1))))
                .andExpect(jsonPath("$[0].id", is(expectedBookResponseDTO.getId().intValue())))
                .andExpect(jsonPath("$[0].name", is(expectedBookResponseDTO.getName())))
                .andExpect(jsonPath("$[0].isbn", is(expectedBookResponseDTO.getIsbn())))
                .andExpect(jsonPath("$[0].chapters", is(expectedBookResponseDTO.getChapters().intValue())))
                .andExpect(jsonPath("$[0].pages", is(expectedBookResponseDTO.getPages().intValue())))
                .andExpect(jsonPath("$[0].author.id", is(expectedBookResponseDTO.getAuthor().getId().intValue())))
                .andExpect(jsonPath("$[0].publisher.id", is(expectedBookResponseDTO.getPublisher().getId().intValue())));

        Mockito.verify(service, Mockito.times(1)).findAll();
    }
}
