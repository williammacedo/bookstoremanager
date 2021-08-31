package com.williammacedo.bookstoremanager.author.controller;

import com.williammacedo.bookstoremanager.author.builder.AuthorDTOBuilder;
import com.williammacedo.bookstoremanager.author.dto.AuthorDTO;
import com.williammacedo.bookstoremanager.author.service.AuthorService;
import com.williammacedo.bookstoremanager.author.utils.JsonConversionUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import java.util.Collections;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class AuthorControllerUnitTest {

    private  static final String AUTHOR_API_URL_PATH = "/v1/authors";

    @Mock
    private AuthorService service;

    @InjectMocks
    private AuthorController controller;

    @Autowired
    private MockMvc mockMvc;

    private AuthorDTOBuilder authorDTOBuilder;

    @BeforeEach
    void setUp() {
        authorDTOBuilder = AuthorDTOBuilder.builder().build();
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .setViewResolvers((s, locale) -> new MappingJackson2JsonView())
                .build();
    }

    @Test
    void whenGETFindAllIsCalledThenItShouldBeReturned() throws Exception {
        AuthorDTO expectedAuthorDTOReturned = authorDTOBuilder.buildAuthorDTO();

        Mockito.when(service.findAll())
                .thenReturn(Collections.singletonList(expectedAuthorDTOReturned));

        mockMvc.perform(MockMvcRequestBuilders.get(AUTHOR_API_URL_PATH))
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(1))))
                .andExpect(jsonPath("$[0].id", is(expectedAuthorDTOReturned.getId().intValue())))
                .andExpect(jsonPath("$[0].name", is(expectedAuthorDTOReturned.getName())))
                .andExpect(jsonPath("$[0].age", is(expectedAuthorDTOReturned.getAge())));
    }

    @Test
    void whenPOSTIsCalledThenStatusCreatdShouldBeReturned() throws Exception {
        AuthorDTO expectedCreatedAuthorDTO = authorDTOBuilder.buildAuthorDTO();

        Mockito.when(service.create(expectedCreatedAuthorDTO))
                .thenReturn(expectedCreatedAuthorDTO);

        mockMvc.perform(
                    MockMvcRequestBuilders.post(AUTHOR_API_URL_PATH)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(JsonConversionUtils.asJsonString(expectedCreatedAuthorDTO))
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(expectedCreatedAuthorDTO.getId().intValue())))
                .andExpect(jsonPath("$.name", is(expectedCreatedAuthorDTO.getName())))
                .andExpect(jsonPath("$.age", is(expectedCreatedAuthorDTO.getAge())));
    }

    @Test
    void whenPOSTIsCalledWithoutRequiredFieldThenBadRequestShouldBeInformed() throws Exception {
        AuthorDTO expectedCreatedAuthorDTO = authorDTOBuilder.buildAuthorDTO();
        expectedCreatedAuthorDTO.setName(null);

        mockMvc.perform(
                    MockMvcRequestBuilders.post(AUTHOR_API_URL_PATH)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(JsonConversionUtils.asJsonString(expectedCreatedAuthorDTO))
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenGETIsCalledWithValidIdThenOkShouldBeReturned() throws Exception {
        AuthorDTO expectedCreatedAuthorDTO = authorDTOBuilder.buildAuthorDTO();

        Mockito.when(service.findById(expectedCreatedAuthorDTO.getId()))
                .thenReturn(expectedCreatedAuthorDTO);

        mockMvc.perform(
                MockMvcRequestBuilders.get(AUTHOR_API_URL_PATH + "/" + expectedCreatedAuthorDTO.getId())
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id", is(expectedCreatedAuthorDTO.getId().intValue())))
            .andExpect(jsonPath("$.name", is(expectedCreatedAuthorDTO.getName())))
            .andExpect(jsonPath("$.age", is(expectedCreatedAuthorDTO.getAge())));
    }

    @Test
    void whenDeleteWithValidIdThenNoContentShouldBeReturned() throws Exception {
        Mockito.doNothing().when(service).delete(1L);

        mockMvc.perform(
                        MockMvcRequestBuilders.delete(AUTHOR_API_URL_PATH + "/" + 1L)
                )
                .andExpect(status().isNoContent());
    }

}
