package com.williammacedo.bookstoremanager.author.controller;

import com.williammacedo.bookstoremanager.author.builder.AuthorDTOBuilder;
import com.williammacedo.bookstoremanager.author.dto.AuthorDTO;
import com.williammacedo.bookstoremanager.author.entity.Author;
import com.williammacedo.bookstoremanager.author.repository.AuthorRepository;
import com.williammacedo.bookstoremanager.author.utils.JsonConversionUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Sql("/authors.sql")
class AuthorControllerTest {

    private  static final String AUTHOR_API_URL_PATH = "/v1/authors";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    AuthorController authorController;

    @Autowired
    AuthorRepository repository;

    private AuthorDTOBuilder authorDTOBuilder;

    @BeforeEach
    void setUp() {
        authorDTOBuilder = AuthorDTOBuilder.builder().build();
    }

    @Test
    @DisplayName("Integration test - Get author by id.")
    void whenGETIsCalledThenReturnAuthor() throws Exception {
        AuthorDTO expectedCreatedAuthorDTO = AuthorDTOBuilder.builder()
                .id(1L).age(33).name("William")
                .build().buildAuthorDTO();

        mockMvc.perform(
            MockMvcRequestBuilders.get(AUTHOR_API_URL_PATH + "/" + expectedCreatedAuthorDTO.getId())
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id", is(expectedCreatedAuthorDTO.getId().intValue())))
            .andExpect(jsonPath("$.name", is(expectedCreatedAuthorDTO.getName())))
            .andExpect(jsonPath("$.age", is(expectedCreatedAuthorDTO.getAge())));
    }

    @Test
    @DisplayName("Integration test - Author not found by id.")
    void whenGETIsCalledWithInvalidIdThenNotFound() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get(AUTHOR_API_URL_PATH + "/" + 10L)
                )
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Integration test - List all authors.")
    void listAuthors() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(AUTHOR_API_URL_PATH))
        .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(3))))
        .andExpect(jsonPath("$[0].name", is("Glayce")));
    }

    @Test
    @DisplayName("Integration test - Create author.")
    void createAuthors() throws Exception {
        AuthorDTO expectedCreatedAuthorDTO = authorDTOBuilder.buildAuthorDTO();
        expectedCreatedAuthorDTO.setId(null);

        mockMvc.perform(
                        MockMvcRequestBuilders.post(AUTHOR_API_URL_PATH)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(JsonConversionUtils.asJsonString(expectedCreatedAuthorDTO))
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.name", is(expectedCreatedAuthorDTO.getName())))
                .andExpect(jsonPath("$.age", is(expectedCreatedAuthorDTO.getAge())));
    }

    @Test
    @DisplayName("Integration test - Should return BAD Request.")
    void whenAuthorWithoutRequiredFieldThenBadRequestShouldBeInformed() throws Exception {
        AuthorDTO expectedCreatedAuthorDTO = authorDTOBuilder.buildAuthorDTO();
        expectedCreatedAuthorDTO.setName(null);

        mockMvc.perform(
                        MockMvcRequestBuilders.post(AUTHOR_API_URL_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonConversionUtils.asJsonString(expectedCreatedAuthorDTO))
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", notNullValue()));
    }

    @Test
    @DisplayName("Integration test - Should delete Author.")
    void whenDeleteAuthorThenNoContent() throws Exception {
        mockMvc.perform(
                    MockMvcRequestBuilders.delete(AUTHOR_API_URL_PATH + "/" + 1L)
                )
                .andExpect(status().isNoContent());

        Optional<Author> optionalAuthor= repository.findById(1L);

        Assertions.assertTrue(optionalAuthor.isEmpty());
    }

    @Test
    @DisplayName("Integration test - Should return Author Not Found.")
    void whenDeleteWithInvalidIdThenNotFound() throws Exception {
        mockMvc.perform(
                        MockMvcRequestBuilders.delete(AUTHOR_API_URL_PATH + "/" + 20L)
                )
                .andExpect(status().isNotFound());
    }

}
