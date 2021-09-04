package com.williammacedo.bookstoremanager.user.service;

import com.williammacedo.bookstoremanager.author.entity.Author;
import com.williammacedo.bookstoremanager.author.exception.AuthorAlreadyExistsException;
import com.williammacedo.bookstoremanager.author.exception.AuthorNotFoundException;
import com.williammacedo.bookstoremanager.user.dto.UserDTO;
import com.williammacedo.bookstoremanager.user.entity.User;
import com.williammacedo.bookstoremanager.user.exception.UserAlreadyExistsException;
import com.williammacedo.bookstoremanager.user.exception.UserNotFoundException;
import com.williammacedo.bookstoremanager.user.mapper.UserMapper;
import com.williammacedo.bookstoremanager.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {
    private static final UserMapper mapper = UserMapper.INSTANCE;

    private UserRepository repository;

    public List<UserDTO> findAll() {
        return repository.findAllAsDTO();
    }

    public UserDTO findById(Long id) {
        User user = verifyAndGetUser(id);
        return mapper.toDTO(user);
    }

    private User verifyAndGetUser(Long id) {
        return repository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
    }

    private void verifyIfExists(String name) {
        repository.findByNameIgnoreCase(name)
                .ifPresent(user -> {throw new UserAlreadyExistsException(user.getName());});
    }
}
