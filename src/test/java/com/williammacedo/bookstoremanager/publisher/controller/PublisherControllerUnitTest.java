package com.williammacedo.bookstoremanager.publisher.controller;

import com.williammacedo.bookstoremanager.author.builder.AuthorDTOBuilder;
import com.williammacedo.bookstoremanager.author.dto.AuthorDTO;
import com.williammacedo.bookstoremanager.publisher.builder.PublisherDTOBuilder;
import com.williammacedo.bookstoremanager.publisher.dto.PublisherDTO;
import com.williammacedo.bookstoremanager.publisher.service.PublisherService;
import com.williammacedo.bookstoremanager.utils.JsonConversionUtils;
import org.junit.jupiter.api.Assertions;
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
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import java.time.format.DateTimeFormatter;
import java.util.Collections;

import static com.williammacedo.bookstoremanager.utils.JsonConversionUtils.asJsonString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class PublisherControllerUnitTest {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private  static final String PUBLISHER_API_URL_PATH = "/v1/publishers";

    @Autowired
    private MockMvc mockMvc;

    private PublisherDTOBuilder dtoBuilder;

    @Mock
    PublisherService service;

    @InjectMocks
    PublisherController controller;

    @BeforeEach
    void setUp() {
        dtoBuilder = PublisherDTOBuilder.builder().build();
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .setViewResolvers((s, locale) -> new MappingJackson2JsonView())
                .build();
    }

    @Test
    @DisplayName("Controller UT - List all publishers")
    void whenGETFindAllIsCalledThenItShouldBeReturned() throws Exception {
        PublisherDTO expectedPublisherDTOReturned = dtoBuilder.buildPublisherDTO();

        Mockito.when(service.findAll())
                .thenReturn(Collections.singletonList(expectedPublisherDTOReturned));

        mockMvc.perform(MockMvcRequestBuilders.get(PUBLISHER_API_URL_PATH))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(1))))
                .andExpect(jsonPath("$[0].id", is(expectedPublisherDTOReturned.getId().intValue())))
                .andExpect(jsonPath("$[0].name", is(expectedPublisherDTOReturned.getName())))
                .andExpect(jsonPath("$[0].code", is(expectedPublisherDTOReturned.getCode())))
                .andExpect(
                    jsonPath("$[0].foundationDate", is(formatter.format(expectedPublisherDTOReturned.getFoundationDate())))
                );
    }

    @Test
    @DisplayName("Controller UT - Find specific publisher by ID")
    void whenGETByIDIsCalledThenItShouldBeReturned() throws Exception {
        PublisherDTO expectedPublisherDTOReturned = dtoBuilder.buildPublisherDTO();

        Mockito.when(service.findById(expectedPublisherDTOReturned.getId()))
                .thenReturn(expectedPublisherDTOReturned);

        mockMvc.perform(MockMvcRequestBuilders.get(PUBLISHER_API_URL_PATH + "/" + expectedPublisherDTOReturned.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(expectedPublisherDTOReturned.getId().intValue())))
                .andExpect(jsonPath("$.name", is(expectedPublisherDTOReturned.getName())))
                .andExpect(jsonPath("$.code", is(expectedPublisherDTOReturned.getCode())))
                .andExpect(
                    jsonPath("$.foundationDate", is(formatter.format(expectedPublisherDTOReturned.getFoundationDate())))
                );
    }

    @Test
    @DisplayName("Controller UT - POST publisher DTO")
    void whenPOSTPublisherIsCalledThenItShouldBeCreated() throws Exception {
        PublisherDTO expectedPublisherDTOReturned = dtoBuilder.buildPublisherDTO();

        Mockito.when(service.create(expectedPublisherDTOReturned))
                .thenReturn(expectedPublisherDTOReturned);

        mockMvc.perform(
            post(PUBLISHER_API_URL_PATH)
            .contentType(MediaType.APPLICATION_JSON)
            .content(asJsonString(expectedPublisherDTOReturned))
        )
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id", is(expectedPublisherDTOReturned.getId().intValue())))
        .andExpect(jsonPath("$.name", is(expectedPublisherDTOReturned.getName())))
        .andExpect(jsonPath("$.code", is(expectedPublisherDTOReturned.getCode())))
        .andExpect(
            jsonPath("$.foundationDate", is(formatter.format(expectedPublisherDTOReturned.getFoundationDate())))
        );
    }

    @Test
    @DisplayName("Controller UT - POST publisher DTO without required fields.")
    void whenPOSTPublisherIsCalledWithoutRequiredFieldsThenBadRequestShouldBeInformed() throws Exception {
        PublisherDTO expectedPublisherDTOReturned = dtoBuilder.buildPublisherDTO();
        expectedPublisherDTOReturned.setName(null);

        mockMvc.perform(
                post(PUBLISHER_API_URL_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(expectedPublisherDTOReturned))
            )
            .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Controller UT - Delete one publisher")
    void whenDELETEThenItShouldNoContent() throws Exception {
        final Long idPublisher = 1L;

        Mockito.doNothing().when(service).delete(idPublisher);

        mockMvc.perform(MockMvcRequestBuilders.delete(PUBLISHER_API_URL_PATH + "/" + idPublisher))
                .andExpect(status().isNoContent());

        Mockito.verify(service, Mockito.times(1)).delete(idPublisher);
    }

}
