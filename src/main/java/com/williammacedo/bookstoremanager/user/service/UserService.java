package com.williammacedo.bookstoremanager.user.service;

import com.williammacedo.bookstoremanager.user.mapper.UserMapper;
import com.williammacedo.bookstoremanager.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {
    private static final UserMapper mapper = UserMapper.INSTANCE;

    private UserRepository repository;
}
