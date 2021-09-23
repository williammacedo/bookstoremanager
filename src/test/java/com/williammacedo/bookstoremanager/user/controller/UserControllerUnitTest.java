package com.williammacedo.bookstoremanager.user.controller;

import com.williammacedo.bookstoremanager.exception.BookstoreExceptionHandler;
import com.williammacedo.bookstoremanager.user.builder.JwtRequestBuilder;
import com.williammacedo.bookstoremanager.user.builder.UserDTOBuilder;
import com.williammacedo.bookstoremanager.user.dto.UserDTO;
import com.williammacedo.bookstoremanager.user.exception.UserAlreadyExistsException;
import com.williammacedo.bookstoremanager.user.exception.UserNotFoundException;
import com.williammacedo.bookstoremanager.user.service.UserService;
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
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class UserControllerUnitTest {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private static final String USERS_API_URL_PATH = "/v1/users";

    @Mock
    UserService service;

    @InjectMocks
    UserController controller;

    @Autowired
    MockMvc mockMvc;

    UserDTOBuilder dtoBuilder;
    JwtRequestBuilder jwtRequestBuilder;

    @BeforeEach
    void setUp() {
        dtoBuilder = UserDTOBuilder.builder().build();
        jwtRequestBuilder = JwtRequestBuilder.builder().build();
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(BookstoreExceptionHandler.class)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .setViewResolvers((s, locale) -> new MappingJackson2JsonView())
                .build();
    }

    @Test
    @DisplayName("Controller UT - List all users")
    void whenGETIsCalledThenShouldReturnAnListOfUsers() throws Exception {
        UserDTO expectedUser = dtoBuilder.buildUserDTO();

        when(service.findAll()).thenReturn(Collections.singletonList(expectedUser));

        mockMvc.perform(MockMvcRequestBuilders.get(USERS_API_URL_PATH))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(equalTo(1))))
                .andExpect(jsonPath("$[0].id", is(expectedUser.getId().intValue())))
                .andExpect(jsonPath("$[0].name", is(expectedUser.getName())))
                .andExpect(jsonPath("$[0].age", is(expectedUser.getAge())))
                .andExpect(jsonPath("$[0].gender", is(expectedUser.getGender().name())))
                .andExpect(jsonPath("$[0].email", is(expectedUser.getEmail())))
                .andExpect(jsonPath("$[0].username", is(expectedUser.getUsername())))
                .andExpect(jsonPath("$[0].password", is(expectedUser.getPassword())))
                .andExpect(
                        jsonPath("$[0].birthDate", is(formatter.format(expectedUser.getBirthDate())))
                );

        Mockito.verify(service, Mockito.times(1)).findAll();
    }

    @Test
    @DisplayName("Controller UT - Find by ID")
    void whenGETIsCalledWithIDThenShouldReturnAnUser() throws Exception {
        UserDTO expectedUser = dtoBuilder.buildUserDTO();

        when(service.findById(expectedUser.getId())).thenReturn(expectedUser);

        mockMvc.perform(MockMvcRequestBuilders.get(USERS_API_URL_PATH + "/" + expectedUser.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(expectedUser.getId().intValue())))
                .andExpect(jsonPath("$.name", is(expectedUser.getName())))
                .andExpect(jsonPath("$.age", is(expectedUser.getAge())))
                .andExpect(jsonPath("$.gender", is(expectedUser.getGender().name())))
                .andExpect(jsonPath("$.email", is(expectedUser.getEmail())))
                .andExpect(jsonPath("$.username", is(expectedUser.getUsername())))
                .andExpect(jsonPath("$.password", is(expectedUser.getPassword())))
                .andExpect(
                        jsonPath("$.birthDate", is(formatter.format(expectedUser.getBirthDate())))
                );

        Mockito.verify(service, Mockito.times(1)).findById(expectedUser.getId());
    }

    @Test
    @DisplayName("Controller UT - Find by With Invalid ID")
    void whenGETIsCalledWithInvalidIDThenShouldReturnNotFound() throws Exception {
        UserDTO expectedUser = dtoBuilder.buildUserDTO();

        when(service.findById(expectedUser.getId())).thenThrow(new UserNotFoundException(expectedUser.getId()));

        mockMvc.perform(MockMvcRequestBuilders.get(USERS_API_URL_PATH + "/" + expectedUser.getId()))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errors", hasSize(equalTo(1))))
                .andExpect(jsonPath("$.errors[0]", is("User with id 100 not exists!")))
                .andExpect(jsonPath("$.message", is("User with id 100 not exists!")));

        Mockito.verify(service, Mockito.times(1)).findById(expectedUser.getId());
    }

    @Test
    @DisplayName("Controller UT - Post to create user")
    void whenPOSTIsCalledThenShouldReturnUserCreated() throws Exception {
        UserDTO expectedUser = dtoBuilder.buildUserDTO();

        when(service.create(expectedUser)).thenReturn(expectedUser);

        mockMvc.perform(
                    post(USERS_API_URL_PATH)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(asJsonString(expectedUser))
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(expectedUser.getId().intValue())))
                .andExpect(jsonPath("$.name", is(expectedUser.getName())))
                .andExpect(jsonPath("$.age", is(expectedUser.getAge())))
                .andExpect(jsonPath("$.gender", is(expectedUser.getGender().name())))
                .andExpect(jsonPath("$.email", is(expectedUser.getEmail())))
                .andExpect(jsonPath("$.username", is(expectedUser.getUsername())))
                .andExpect(jsonPath("$.password", is(expectedUser.getPassword())))
                .andExpect(
                        jsonPath("$.birthDate", is(formatter.format(expectedUser.getBirthDate())))
                );

        Mockito.verify(service, Mockito.times(1)).create(expectedUser);
    }

    @Test
    @DisplayName("Controller UT - Post user without required fields then Bad Request")
    void whenPOSTIsCalledWithoutRequiredFieldsThenShouldReturnBadRequest() throws Exception {
        UserDTO expectedUser = dtoBuilder.buildUserDTO();
        expectedUser.setName(null);

        mockMvc.perform(
                    post(USERS_API_URL_PATH)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(asJsonString(expectedUser))
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors", hasSize(equalTo(1))))
                .andExpect(jsonPath("$.errors[0]", is("Field: NAME must not be blank")))
                .andExpect(jsonPath("$.message", is("Informed argument(s) validation error(s)")));

        Mockito.verify(service, Mockito.times(0)).create(expectedUser);
    }

    @Test
    @DisplayName("Controller UT - Put to update user")
    void whenPUTIsCalledThenShouldReturnUserUpdated() throws Exception {
        UserDTO expectedUser = dtoBuilder.buildUserDTO();

        when(service.update(expectedUser.getId(), expectedUser)).thenReturn(expectedUser);

        mockMvc.perform(
                        MockMvcRequestBuilders.put(USERS_API_URL_PATH + "/" + expectedUser.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(asJsonString(expectedUser))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(expectedUser.getId().intValue())))
                .andExpect(jsonPath("$.name", is(expectedUser.getName())))
                .andExpect(jsonPath("$.age", is(expectedUser.getAge())))
                .andExpect(jsonPath("$.gender", is(expectedUser.getGender().name())))
                .andExpect(jsonPath("$.email", is(expectedUser.getEmail())))
                .andExpect(jsonPath("$.username", is(expectedUser.getUsername())))
                .andExpect(jsonPath("$.password", is(expectedUser.getPassword())))
                .andExpect(
                        jsonPath("$.birthDate", is(formatter.format(expectedUser.getBirthDate())))
                );

        Mockito.verify(service, Mockito.times(1)).update(expectedUser.getId(), expectedUser);
    }

    @Test
    @DisplayName("Controller UT - Put user with invalid ID")
    void whenPUTIsCalledWithInvalidIDThenShouldReturnNotFound() throws Exception {
        UserDTO expectedUser = dtoBuilder.buildUserDTO();
        String exceptionExpected = String.format("User with id %s not exists!", expectedUser.getId());

        Mockito.doThrow(new UserNotFoundException(expectedUser.getId()))
                .when(service).update(expectedUser.getId(), expectedUser);

        mockMvc.perform(
                    MockMvcRequestBuilders.put(USERS_API_URL_PATH + "/" + expectedUser.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(asJsonString(expectedUser))
                )
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errors", hasSize(equalTo(1))))
                .andExpect(jsonPath("$.errors[0]", is(exceptionExpected)))
                .andExpect(jsonPath("$.message", is(exceptionExpected)));

        Mockito.verify(service, Mockito.times(1)).update(expectedUser.getId(), expectedUser);
    }

    @Test
    @DisplayName("Controller UT - Put user with email or username already exists")
    void whenPUTIsCalledWithUsernameOrEmailAlreadyExistsInDatabaseThenShouldReturnBadRequest() throws Exception {
        UserDTO expectedUser = dtoBuilder.buildUserDTO();
        String exceptionExpected = String.format("User with email %s or username %s already exists!",
                expectedUser.getEmail(), expectedUser.getUsername());

        Mockito.doThrow(new UserAlreadyExistsException(expectedUser.getUsername(), expectedUser.getEmail()))
                .when(service).update(expectedUser.getId(), expectedUser);

        mockMvc.perform(
                    MockMvcRequestBuilders.put(USERS_API_URL_PATH + "/" + expectedUser.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(asJsonString(expectedUser))
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors", hasSize(equalTo(1))))
                .andExpect(jsonPath("$.errors[0]", is(exceptionExpected)))
                .andExpect(jsonPath("$.message", is(exceptionExpected)));

        Mockito.verify(service, Mockito.times(1)).update(expectedUser.getId(), expectedUser);
    }

    @Test
    @DisplayName("Controller UT - Put user without required fields then Bad Request")
    void whenPUTIsCalledWithoutRequiredFieldsThenShouldReturnBadRequest() throws Exception {
        UserDTO expectedUser = dtoBuilder.buildUserDTO();
        expectedUser.setName(null);

        mockMvc.perform(
                        MockMvcRequestBuilders.put(USERS_API_URL_PATH + "/" + expectedUser.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(asJsonString(expectedUser))
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors", hasSize(equalTo(1))))
                .andExpect(jsonPath("$.errors[0]", is("Field: NAME must not be blank")))
                .andExpect(jsonPath("$.message", is("Informed argument(s) validation error(s)")));
    }

    @Test
    @DisplayName("Controller UT - Delete by ID")
    void whenDELETEIsCalledWithIDThenShouldReturnNoContent() throws Exception {
        UserDTO expectedUser = dtoBuilder.buildUserDTO();

        Mockito.doNothing().when(service).delete(expectedUser.getId());

        mockMvc.perform(MockMvcRequestBuilders.delete(USERS_API_URL_PATH + "/" + expectedUser.getId()))
                .andExpect(status().isNoContent());

        Mockito.verify(service, Mockito.times(1)).delete(expectedUser.getId());
    }

    @Test
    @DisplayName("Controller UT - Delete by With Invalid ID")
    void whenDELETEIsCalledWithInvalidIDThenShouldReturnNotFound() throws Exception {
        UserDTO expectedUser = dtoBuilder.buildUserDTO();

        Mockito.doThrow(new UserNotFoundException(expectedUser.getId())).when(service).delete(expectedUser.getId());

        mockMvc.perform(MockMvcRequestBuilders.delete(USERS_API_URL_PATH + "/" + expectedUser.getId()))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errors", hasSize(equalTo(1))))
                .andExpect(jsonPath("$.errors[0]", is("User with id 100 not exists!")))
                .andExpect(jsonPath("$.message", is("User with id 100 not exists!")));

        Mockito.verify(service, Mockito.times(1)).delete(expectedUser.getId());
    }
}
