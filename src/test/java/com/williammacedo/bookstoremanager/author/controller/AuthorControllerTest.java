package com.williammacedo.bookstoremanager.author.controller;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Sql("/authors.sql")
class AuthorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    AuthorController authorController;

    @Test
    void listAuthors() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/v1/authors"))
        .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(3))))
        .andExpect(jsonPath("$[0].name", is("Glayce")));
    }

}
