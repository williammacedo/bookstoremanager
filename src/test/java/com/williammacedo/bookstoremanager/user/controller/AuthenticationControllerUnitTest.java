package com.williammacedo.bookstoremanager.user.controller;

import com.williammacedo.bookstoremanager.exception.BookstoreExceptionHandler;
import com.williammacedo.bookstoremanager.user.builder.JwtRequestBuilder;
import com.williammacedo.bookstoremanager.user.dto.AuthenticatedUser;
import com.williammacedo.bookstoremanager.user.dto.JwtRequest;
import com.williammacedo.bookstoremanager.user.dto.JwtResponse;
import com.williammacedo.bookstoremanager.user.enums.Role;
import com.williammacedo.bookstoremanager.user.service.AuthenticationService;
import com.williammacedo.bookstoremanager.user.service.JwtTokenManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import static com.williammacedo.bookstoremanager.utils.JsonConversionUtils.asJsonString;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class AuthenticationControllerUnitTest {

    @Mock
    AuthenticationService authenticationService;

    @Mock
    AuthenticationManager authenticationManager;

    @Mock
    JwtTokenManager jwtTokenManager;

    @InjectMocks
    AuthenticationController controller;

    @Autowired
    MockMvc mockMvc;

    JwtRequestBuilder jwtRequestBuilder;

    @BeforeEach
    void setUp() {
        jwtRequestBuilder = JwtRequestBuilder.builder().build();
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(BookstoreExceptionHandler.class)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .setViewResolvers((s, locale) -> new MappingJackson2JsonView())
                .build();
    }

    @Test
    @DisplayName("Valid authentication")
    void whenPOSTIsCalledToAuthenticateUserThenOkShouldBeInformed() throws Exception {
        final String username = "william";
        final String password = "123456";
        final Role role = Role.USER;
        final String expectedGeneratedToken = "fakeToken";

        JwtRequest jwtRequest = jwtRequestBuilder.buildJwtRequest();
        JwtResponse expectedJwtToken = new JwtResponse("fakeToken");

        when(authenticationService.loadUserByUsername(username)).thenReturn(new AuthenticatedUser(username, password, role.name()));
        when(jwtTokenManager.generateToken(any(UserDetails.class))).thenReturn(expectedGeneratedToken);

        mockMvc.perform(post("/v1/auth")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(jwtRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.jwtToken", is(expectedJwtToken.getJwtToken())));

        verify(authenticationService, times(1)).loadUserByUsername(username);
    }

    @Test
    @DisplayName("Authenticated without required parameters")
    void whenPOSTIsCalledToAuthenticateUserWithoutPasswordThenBadRequestShouldBeInformed() throws Exception {
        JwtRequest jwtRequest = jwtRequestBuilder.buildJwtRequest();
        jwtRequest.setPassword(null);

        mockMvc.perform(post("/v1/auth")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(jwtRequest)))
                .andExpect(status().isBadRequest());
    }
}
