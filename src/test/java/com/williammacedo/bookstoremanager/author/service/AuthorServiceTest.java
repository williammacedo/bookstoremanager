package com.williammacedo.bookstoremanager.author.service;

import com.williammacedo.bookstoremanager.author.builder.AuthorDTOBuilder;
import com.williammacedo.bookstoremanager.author.mapper.AuthorMapper;
import com.williammacedo.bookstoremanager.author.repository.AuthorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;

@RunWith(MockitoJUnitRunner.class)
public class AuthorServiceTest {

    @Autowired
    private AuthorMapper mapper;

    @Mock
    private AuthorRepository repository;

    @InjectMocks
    private AuthorService service;

    private AuthorDTOBuilder authorDTOBuilder;

    @BeforeEach
    void setUp() {
        authorDTOBuilder = AuthorDTOBuilder.builder().build();
    }
}
