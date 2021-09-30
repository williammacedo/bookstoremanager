package com.williammacedo.bookstoremanager.author.service;

import com.williammacedo.bookstoremanager.author.dto.AuthorDTO;
import com.williammacedo.bookstoremanager.author.entity.Author;
import com.williammacedo.bookstoremanager.author.exception.AuthorAlreadyExistsException;
import com.williammacedo.bookstoremanager.author.exception.AuthorNotFoundException;
import com.williammacedo.bookstoremanager.author.mapper.AuthorMapper;
import com.williammacedo.bookstoremanager.author.repository.AuthorRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@AllArgsConstructor
@Service
@Transactional(readOnly = true)
public class AuthorService {

    private static final AuthorMapper mapper = AuthorMapper.INSTANCE;

    private AuthorRepository repository;

    public AuthorDTO findById(Long id) {
        Author author = verifyAndGetAuthor(id);
        return mapper.toDTO(author);
    }

    public List<AuthorDTO> findAll() {
        return repository.findAllAsDTO(Sort.by("name"));
    }

    @Transactional
    public AuthorDTO create(AuthorDTO dto) {
        verifyIfExists(dto.getName());

        Author authorToCreate = mapper.toModel(dto);
        Author createdAuthor = repository.save(authorToCreate);
        return mapper.toDTO(createdAuthor);
    }

    @Transactional
    public void delete(Long id) {
        Author author = verifyAndGetAuthor(id);
        repository.delete(author);
    }

    public Author verifyAndGetAuthor(Long id) {
        return repository.findById(id).orElseThrow(() -> new AuthorNotFoundException(id));
    }

    private void verifyIfExists(String authorName) {
        repository.findByNameIgnoreCase(authorName)
                .ifPresent(author -> {throw new AuthorAlreadyExistsException(author.getName());});
    }
}
