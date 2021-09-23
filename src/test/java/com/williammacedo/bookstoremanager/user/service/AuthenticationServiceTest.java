package com.williammacedo.bookstoremanager.user.service;

import com.williammacedo.bookstoremanager.user.builder.JwtRequestBuilder;
import com.williammacedo.bookstoremanager.user.dto.JwtRequest;
import com.williammacedo.bookstoremanager.user.dto.JwtResponse;
import com.williammacedo.bookstoremanager.user.dto.UserDetailsDTO;
import com.williammacedo.bookstoremanager.user.enums.Role;
import com.williammacedo.bookstoremanager.user.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest {

    @Mock
    UserRepository repository;

    @InjectMocks
    AuthenticationService service;

    private static JwtRequestBuilder jwtRequestBuilder;

    @BeforeAll
    static void setUp() {
        jwtRequestBuilder = JwtRequestBuilder.builder().build();
    }

    @Test
    @DisplayName("Valid loadByUsername")
    public void whenValidUsernameThenReturnUserDetails() {
        final String username = "william";
        final String password = "123456";
        final Role role = Role.USER;
        final SimpleGrantedAuthority expectRole = new SimpleGrantedAuthority("ROLE_" + role.name());

        when(repository.findByUsername(username))
            .thenReturn(Optional.of(new UserDetailsDTO(username, password, role)));

        UserDetails userDetails = service.loadUserByUsername(username);

        Assertions.assertNotNull(userDetails);
        Assertions.assertEquals(username, userDetails.getUsername());
        Assertions.assertEquals(password, userDetails.getPassword());
        Assertions.assertFalse(userDetails.getAuthorities().isEmpty());
        Assertions.assertTrue(userDetails.getAuthorities().contains(expectRole));

        verify(repository, times(1)).findByUsername(username);
    }

    @Test
    @DisplayName("Valid loadByUsername")
    public void whenInvalidUsernameIsInformedThenExceptionShouldBeThrow() {
        final String username = "william";

        when(repository.findByUsername(username))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(UsernameNotFoundException.class, () -> service.loadUserByUsername(username));

        verify(repository, times(1)).findByUsername(username);
    }
}
