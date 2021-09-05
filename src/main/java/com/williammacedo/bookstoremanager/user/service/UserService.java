package com.williammacedo.bookstoremanager.user.service;

import com.williammacedo.bookstoremanager.user.dto.UserDTO;
import com.williammacedo.bookstoremanager.user.entity.User;
import com.williammacedo.bookstoremanager.user.exception.UserAlreadyExistsException;
import com.williammacedo.bookstoremanager.user.exception.UserNotFoundException;
import com.williammacedo.bookstoremanager.user.mapper.UserMapper;
import com.williammacedo.bookstoremanager.user.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
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

    @Transactional
    public UserDTO create(UserDTO dto) {
        verifyIfExists(dto.getUsername(), dto.getEmail());
        User user = repository.save(mapper.toModel(dto));
        return mapper.toDTO(user);
    }

    @Transactional
    public void delete(Long id) {
        User user = verifyAndGetUser(id);
        repository.delete(user);
    }

    private User verifyAndGetUser(Long id) {
        return repository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
    }

    private void verifyIfExists(String username, String email) {
        repository.findByUsernameOrEmail(username, email)
                .ifPresent(user -> {throw new UserAlreadyExistsException(username, email);});
    }
}
