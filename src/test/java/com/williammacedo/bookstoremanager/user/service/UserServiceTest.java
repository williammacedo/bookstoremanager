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

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    static final UserMapper mapper = UserMapper.INSTANCE;

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
        Mockito.when(repository.findAllAsDTO()).thenReturn(Collections.singletonList(expectedUserDto));

        List<UserDTO> users = service.findAll();
        Assertions.assertNotNull(users);
        Assertions.assertFalse(users.isEmpty());
        Assertions.assertEquals(1, users.size());
        Assertions.assertEquals(expectedUserDto, users.get(0));
    }

    @Test
    @DisplayName("Unit test - Return Empty User List")
    void whenListUserIsCalledAndHasNoUserSavedThenItShouldReturnEmptyList() {
        Mockito.when(repository.findAllAsDTO()).thenReturn(Collections.EMPTY_LIST);

        List<UserDTO> users = service.findAll();
        Assertions.assertNotNull(users);
        Assertions.assertTrue(users.isEmpty());

        Mockito.verify(repository, Mockito.times(1)).findAllAsDTO();
    }

    @Test
    @DisplayName("Unit test - Get User By ID")
    void whenGETUserByIDIsCalledThenItShouldReturn() {
        UserDTO expectedUserDTO = dtoBuilder.buildUserDTO();
        User user = mapper.toModel(expectedUserDTO);
        Mockito.when(repository.findById(expectedUserDTO.getId())).thenReturn(Optional.of(user));

        UserDTO userDTOReturned = service.findById(expectedUserDTO.getId());
        Assertions.assertNotNull(userDTOReturned);
        Assertions.assertEquals(expectedUserDTO, userDTOReturned);

        Mockito.verify(repository, Mockito.times(1)).findById(expectedUserDTO.getId());
    }

    @Test
    @DisplayName("Unit test - Get User By InvalidID -> NotFound")
    void whenGETUserWithInvalidIDIsCalledThenNotFoundShouldBeInformed() {
        final Long invalidID = 100L;
        Mockito.when(repository.findById(invalidID)).thenReturn(Optional.empty());

        Assertions.assertThrows(UserNotFoundException.class, () ->service.findById(invalidID));

        Mockito.verify(repository, Mockito.times(1)).findById(invalidID);
    }

    @Test
    @DisplayName("Unit test - Delete User")
    void whenDELETEUserIsCalledThenItShouldBeDeleted() {
        UserDTO expectedUserDTO = dtoBuilder.buildUserDTO();
        User user = mapper.toModel(expectedUserDTO);

        Mockito.when(repository.findById(expectedUserDTO.getId())).thenReturn(Optional.of(user));
        Mockito.doNothing().when(repository).delete(user);

        service.delete(expectedUserDTO.getId());

        Mockito.verify(repository, Mockito.times(1)).findById(expectedUserDTO.getId());
        Mockito.verify(repository, Mockito.times(1)).delete(user);
    }

    @Test
    @DisplayName("Unit test - Delete User By InvalidID -> NotFound")
    void whenDELETEUserWithInvalidIDIsCalledThenNotFoundShouldBeInformed() {
        final Long invalidID = 100L;
        Mockito.when(repository.findById(invalidID)).thenReturn(Optional.empty());

        Assertions.assertThrows(UserNotFoundException.class, () ->service.delete(invalidID));

        Mockito.verify(repository, Mockito.times(1)).findById(invalidID);
    }

    @Test
    @DisplayName("Unit test - Post return dto")
    void whenPOSTUserIsCalledThenDtoShouldBeReturned() {
        UserDTO expectedUserDTO = dtoBuilder.buildUserDTO();
        User user = mapper.toModel(expectedUserDTO);
        Mockito.when(repository.findByUsernameOrEmail(expectedUserDTO.getUsername(), expectedUserDTO.getEmail()))
                .thenReturn(Optional.empty());
        Mockito.when(repository.save(user))
                .thenReturn(user);

        UserDTO userDTOReturned = service.create(expectedUserDTO);
        Assertions.assertNotNull(userDTOReturned);
        Assertions.assertEquals(expectedUserDTO, userDTOReturned);

        Mockito.verify(repository, Mockito.times(1))
                .findByUsernameOrEmail(expectedUserDTO.getUsername(), expectedUserDTO.getEmail());
        Mockito.verify(repository, Mockito.times(1))
                .save(user);
    }

    @Test
    @DisplayName("Unit test - Post with username or email already exists -> UserAlreadyExists")
    void whenPOSTUserWithUsernameOrEmailAlreadyExistsIsCalledThenAlreadyExistsException() {
        UserDTO expectedUserDTO = dtoBuilder.buildUserDTO();
        User user = mapper.toModel(expectedUserDTO);
        Mockito.when(repository.findByUsernameOrEmail(expectedUserDTO.getUsername(), expectedUserDTO.getEmail()))
                .thenReturn(Optional.empty());
        Mockito.when(repository.findByUsernameOrEmail(expectedUserDTO.getUsername(), expectedUserDTO.getEmail()))
                .thenReturn(Optional.of(user));

        Assertions.assertThrows(UserAlreadyExistsException.class, () ->service.create(expectedUserDTO));

        Mockito.verify(repository, Mockito.times(1))
                .findByUsernameOrEmail(expectedUserDTO.getUsername(), expectedUserDTO.getEmail());
    }

    @Test
    @DisplayName("Unit test - Put return dto")
    void whenPUTUserIsCalledThenDtoShouldBeReturned() {
        UserDTO expectedUserDTO = dtoBuilder.buildUserDTO();
        User user = mapper.toModel(expectedUserDTO);

        Mockito.when(repository.findByUsernameOrEmailAndIdNot(expectedUserDTO.getId(), expectedUserDTO.getUsername(), expectedUserDTO.getEmail()))
                .thenReturn(Optional.empty());
        Mockito.when(repository.findById(expectedUserDTO.getId())).thenReturn(Optional.of(user));
        Mockito.when(repository.save(user))
                .thenReturn(user);

        UserDTO userDTOReturned = service.update(expectedUserDTO.getId(), expectedUserDTO);
        Assertions.assertNotNull(userDTOReturned);
        Assertions.assertEquals(expectedUserDTO, userDTOReturned);

        Mockito.verify(repository, Mockito.times(1))
                .findByUsernameOrEmailAndIdNot(expectedUserDTO.getId(), expectedUserDTO.getUsername(), expectedUserDTO.getEmail());
        Mockito.verify(repository, Mockito.times(1))
                .findById(expectedUserDTO.getId());
        Mockito.verify(repository, Mockito.times(1))
                .save(user);
    }

    @Test
    @DisplayName("Unit test - Put Invalid ID -> 404")
    void whenPUTUserIsCalledWithInvalidIDThenDtoShouldBeReturnedNotFound() {
        UserDTO expectedUserDTO = dtoBuilder.buildUserDTO();

        Mockito.when(repository.findByUsernameOrEmailAndIdNot(expectedUserDTO.getId(), expectedUserDTO.getUsername(), expectedUserDTO.getEmail()))
                .thenReturn(Optional.empty());
        Mockito.when(repository.findById(expectedUserDTO.getId())).thenReturn(Optional.empty());

        Assertions.assertThrows(UserNotFoundException.class, () ->service.update(expectedUserDTO.getId(), expectedUserDTO));

        Mockito.verify(repository, Mockito.times(1))
                .findByUsernameOrEmailAndIdNot(expectedUserDTO.getId(), expectedUserDTO.getUsername(), expectedUserDTO.getEmail());
        Mockito.verify(repository, Mockito.times(1))
                .findById(expectedUserDTO.getId());
    }

    @Test
    @DisplayName("Unit test - Put Username Or Email Already Exists -> 400")
    void whenPUTUserIsCalledUpdateUsernameOrEmailAlreadyExistsThenDtoShouldBeReturnedBadRequest() {
        UserDTO expectedUserDTO = dtoBuilder.buildUserDTO();
        User user = mapper.toModel(expectedUserDTO);

        Mockito.when(repository.findByUsernameOrEmailAndIdNot(expectedUserDTO.getId(), expectedUserDTO.getUsername(), expectedUserDTO.getEmail()))
                .thenReturn(Optional.of(user));

        Assertions.assertThrows(UserAlreadyExistsException.class, () ->service.update(expectedUserDTO.getId(), expectedUserDTO));

        Mockito.verify(repository, Mockito.times(1))
                .findByUsernameOrEmailAndIdNot(expectedUserDTO.getId(), expectedUserDTO.getUsername(), expectedUserDTO.getEmail());
    }
}
