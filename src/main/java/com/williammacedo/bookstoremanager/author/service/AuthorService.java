package com.williammacedo.bookstoremanager.author.service;

import com.williammacedo.bookstoremanager.author.dto.AuthorDTO;
import com.williammacedo.bookstoremanager.author.mapper.AuthorMapper;
import com.williammacedo.bookstoremanager.author.repository.AuthorRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class AuthorService {

    private final static AuthorMapper authorMapper = AuthorMapper.INSTANCE;

    private AuthorRepository repository;

    public List<AuthorDTO> findAll() {
        return repository.findAll().stream()
            .map(authorMapper::toDTO).collect(Collectors.toList());
    }
}
