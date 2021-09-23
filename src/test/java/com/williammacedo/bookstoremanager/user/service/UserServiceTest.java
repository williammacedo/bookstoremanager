package com.williammacedo.bookstoremanager.user.service;

import com.williammacedo.bookstoremanager.user.builder.UserDTOBuilder;
import com.williammacedo.bookstoremanager.user.dto.UserDTO;
import com.williammacedo.bookstoremanager.user.entity.User;
import com.williammacedo.bookstoremanager.user.exception.UserAlreadyExistsException;
import com.williammacedo.bookstoremanager.user.exception.UserNotFoundException;
import com.williammacedo.bookstoremanager.user.mapper.UserMapper;
import com.williammacedo.bookstoremanager.user.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    static final UserMapper mapper = UserMapper.INSTANCE;

    @Mock
    PasswordEncoder passwordEncoder;

    @Mock
    UserRepository repository;

    @InjectMocks
    UserService service;

    static UserDTOBuilder dtoBuilder;

    @BeforeAll
    static void setUp() {
        dtoBuilder = UserDTOBuilder.builder().build();
    }

    @Test
    @DisplayName("Unit test - List all users")
    void whenListUserIsCalledThenItShouldBeReturned() {
        UserDTO expectedUserDto = dtoBuilder.buildUserDTO();
        when(repository.findAllAsDTO()).thenReturn(Collections.singletonList(expectedUserDto));

        List<UserDTO> users = service.findAll();
        Assertions.assertNotNull(users);
        Assertions.assertFalse(users.isEmpty());
        Assertions.assertEquals(1, users.size());
        Assertions.assertEquals(expectedUserDto, users.get(0));
    }

    @Test
    @DisplayName("Unit test - Return Empty User List")
    void whenListUserIsCalledAndHasNoUserSavedThenItShouldReturnEmptyList() {
        when(repository.findAllAsDTO()).thenReturn(Collections.EMPTY_LIST);

        List<UserDTO> users = service.findAll();
        Assertions.assertNotNull(users);
        Assertions.assertTrue(users.isEmpty());

        verify(repository, Mockito.times(1)).findAllAsDTO();
    }

    @Test
    @DisplayName("Unit test - Get User By ID")
    void whenGETUserByIDIsCalledThenItShouldReturn() {
        UserDTO expectedUserDTO = dtoBuilder.buildUserDTO();
        User user = mapper.toModel(expectedUserDTO);
        when(repository.findById(expectedUserDTO.getId())).thenReturn(Optional.of(user));

        UserDTO userDTOReturned = service.findById(expectedUserDTO.getId());
        Assertions.assertNotNull(userDTOReturned);
        Assertions.assertEquals(expectedUserDTO, userDTOReturned);

        verify(repository, Mockito.times(1)).findById(expectedUserDTO.getId());
    }

    @Test
    @DisplayName("Unit test - Get User By InvalidID -> NotFound")
    void whenGETUserWithInvalidIDIsCalledThenNotFoundShouldBeInformed() {
        final Long invalidID = 100L;
        when(repository.findById(invalidID)).thenReturn(Optional.empty());

        Assertions.assertThrows(UserNotFoundException.class, () ->service.findById(invalidID));

        verify(repository, Mockito.times(1)).findById(invalidID);
    }

    @Test
    @DisplayName("Unit test - Delete User")
    void whenDELETEUserIsCalledThenItShouldBeDeleted() {
        UserDTO expectedUserDTO = dtoBuilder.buildUserDTO();
        User user = mapper.toModel(expectedUserDTO);

        when(repository.findById(expectedUserDTO.getId())).thenReturn(Optional.of(user));
        Mockito.doNothing().when(repository).delete(user);

        service.delete(expectedUserDTO.getId());

        verify(repository, Mockito.times(1)).findById(expectedUserDTO.getId());
        verify(repository, Mockito.times(1)).delete(user);
    }

    @Test
    @DisplayName("Unit test - Delete User By InvalidID -> NotFound")
    void whenDELETEUserWithInvalidIDIsCalledThenNotFoundShouldBeInformed() {
        final Long invalidID = 100L;
        when(repository.findById(invalidID)).thenReturn(Optional.empty());

        Assertions.assertThrows(UserNotFoundException.class, () ->service.delete(invalidID));

        verify(repository, Mockito.times(1)).findById(invalidID);
    }

    @Test
    @DisplayName("Unit test - Post return dto")
    void whenPOSTUserIsCalledThenDtoShouldBeReturned() {
        UserDTO expectedUserDTO = dtoBuilder.buildUserDTO();
        User user = mapper.toModel(expectedUserDTO);
        when(repository.findByUsernameOrEmail(expectedUserDTO.getUsername(), expectedUserDTO.getEmail())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(user.getPassword())).thenReturn(user.getPassword());
        when(repository.save(user)).thenReturn(user);

        UserDTO userDTOReturned = service.create(expectedUserDTO);
        Assertions.assertNotNull(userDTOReturned);
        Assertions.assertEquals(expectedUserDTO, userDTOReturned);

        verify(repository, Mockito.times(1)).findByUsernameOrEmail(expectedUserDTO.getUsername(), expectedUserDTO.getEmail());
        verify(passwordEncoder, Mockito.times(1)).encode(user.getPassword());
        verify(repository, Mockito.times(1)).save(user);
    }

    @Test
    @DisplayName("Unit test - Post with username or email already exists -> UserAlreadyExists")
    void whenPOSTUserWithUsernameOrEmailAlreadyExistsIsCalledThenAlreadyExistsException() {
        UserDTO expectedUserDTO = dtoBuilder.buildUserDTO();
        User user = mapper.toModel(expectedUserDTO);
        when(repository.findByUsernameOrEmail(expectedUserDTO.getUsername(), expectedUserDTO.getEmail()))
                .thenReturn(Optional.of(user));

        Assertions.assertThrows(UserAlreadyExistsException.class, () ->service.create(expectedUserDTO));

        verify(repository, Mockito.times(1))
                .findByUsernameOrEmail(expectedUserDTO.getUsername(), expectedUserDTO.getEmail());
    }

    @Test
    @DisplayName("Unit test - Put return dto")
    void whenPUTUserIsCalledThenDtoShouldBeReturned() {
        UserDTO expectedUserDTO = dtoBuilder.buildUserDTO();
        User user = mapper.toModel(expectedUserDTO);

        when(repository.findByUsernameOrEmailAndIdNot(expectedUserDTO.getId(), expectedUserDTO.getUsername(), expectedUserDTO.getEmail()))
                .thenReturn(Optional.empty());
        when(repository.findById(expectedUserDTO.getId())).thenReturn(Optional.of(user));
        when(passwordEncoder.encode(user.getPassword())).thenReturn(user.getPassword());
        when(repository.save(user))
                .thenReturn(user);

        UserDTO userDTOReturned = service.update(expectedUserDTO.getId(), expectedUserDTO);
        Assertions.assertNotNull(userDTOReturned);
        Assertions.assertEquals(expectedUserDTO, userDTOReturned);

        verify(repository, Mockito.times(1))
                .findByUsernameOrEmailAndIdNot(expectedUserDTO.getId(), expectedUserDTO.getUsername(), expectedUserDTO.getEmail());
        verify(repository, Mockito.times(1))
                .findById(expectedUserDTO.getId());
        verify(passwordEncoder, Mockito.times(1)).encode(user.getPassword());
        verify(repository, Mockito.times(1))
                .save(user);
    }

    @Test
    @DisplayName("Unit test - Put Invalid ID -> 404")
    void whenPUTUserIsCalledWithInvalidIDThenDtoShouldBeReturnedNotFound() {
        UserDTO expectedUserDTO = dtoBuilder.buildUserDTO();
        final Long id = expectedUserDTO.getId();

        when(repository.findByUsernameOrEmailAndIdNot(id, expectedUserDTO.getUsername(), expectedUserDTO.getEmail()))
                .thenReturn(Optional.empty());
        when(repository.findById(id)).thenReturn(Optional.empty());

        Assertions.assertThrows(UserNotFoundException.class, () ->service.update(id, expectedUserDTO));

        verify(repository, Mockito.times(1))
                .findByUsernameOrEmailAndIdNot(id, expectedUserDTO.getUsername(), expectedUserDTO.getEmail());
        verify(repository, Mockito.times(1))
                .findById(id);
    }

    @Test
    @DisplayName("Unit test - Put Username Or Email Already Exists -> 400")
    void whenPUTUserIsCalledUpdateUsernameOrEmailAlreadyExistsThenDtoShouldBeReturnedBadRequest() {
        UserDTO expectedUserDTO = dtoBuilder.buildUserDTO();
        User user = mapper.toModel(expectedUserDTO);
        final Long id = expectedUserDTO.getId();

        when(repository.findByUsernameOrEmailAndIdNot(id, expectedUserDTO.getUsername(), expectedUserDTO.getEmail()))
                .thenReturn(Optional.of(user));

        Assertions.assertThrows(UserAlreadyExistsException.class, () ->service.update(id, expectedUserDTO));

        verify(repository, Mockito.times(1))
                .findByUsernameOrEmailAndIdNot(id, expectedUserDTO.getUsername(), expectedUserDTO.getEmail());
    }
}
