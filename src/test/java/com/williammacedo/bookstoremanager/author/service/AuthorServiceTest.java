package com.williammacedo.bookstoremanager.author.service;

import com.williammacedo.bookstoremanager.author.builder.AuthorDTOBuilder;
import com.williammacedo.bookstoremanager.author.dto.AuthorDTO;
import com.williammacedo.bookstoremanager.author.entity.Author;
import com.williammacedo.bookstoremanager.author.exception.AuthorAlreadyExistsException;
import com.williammacedo.bookstoremanager.author.exception.AuthorNotFoundException;
import com.williammacedo.bookstoremanager.author.mapper.AuthorMapper;
import com.williammacedo.bookstoremanager.author.repository.AuthorRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class AuthorServiceTest {

    private final AuthorMapper mapper = AuthorMapper.INSTANCE;

    @Mock
    private AuthorRepository repository;

    @InjectMocks
    private AuthorService service;

    private static AuthorDTOBuilder authorDTOBuilder;

    @BeforeAll
    static void setUp() {
        authorDTOBuilder = AuthorDTOBuilder.builder().build();
    }

    @Test
    void whenNewAuthorIsInformedThenItsShouldBeCreated() {
        AuthorDTO expectedAuthorToCreateDTO = authorDTOBuilder.buildAuthorDTO();
        Author expectedCreatedAuthor = mapper.toModel(expectedAuthorToCreateDTO);

        Mockito.when(repository.save(expectedCreatedAuthor)).thenReturn(expectedCreatedAuthor);
        Mockito.when(repository.findByNameIgnoreCase(expectedCreatedAuthor.getName())).thenReturn(Optional.empty());

        AuthorDTO createdAuthorDTO = service.create(expectedAuthorToCreateDTO);
        Assertions.assertEquals(createdAuthorDTO, expectedAuthorToCreateDTO);
    }

    @Test
    void whenExistsAuthorIsInformedThenItsShouldBeThrow() {
        AuthorDTO expectedAuthorToCreateDTO = authorDTOBuilder.buildAuthorDTO();
        Author expectedCreatedAuthor = mapper.toModel(expectedAuthorToCreateDTO);

        Mockito
            .when(repository.findByNameIgnoreCase(expectedCreatedAuthor.getName()))
            .thenReturn(Optional.of(expectedCreatedAuthor));

        Assertions.assertThrows(AuthorAlreadyExistsException.class, () -> service.create(expectedAuthorToCreateDTO));
    }

    @Test
    void whenValidIdIsGivenThenAnAuthorShouldBeReturned() {
        AuthorDTO expectedAuthorDTOReturned = authorDTOBuilder.buildAuthorDTO();
        Author expectedAuthor = mapper.toModel(expectedAuthorDTOReturned);

        Mockito
                .when(repository.findById(expectedAuthor.getId()))
                .thenReturn(Optional.of(expectedAuthor));

        AuthorDTO authorReturned = service.findById(expectedAuthor.getId());
        Assertions.assertEquals(authorReturned, expectedAuthorDTOReturned);
    }

    @Test
    void whenInvalidIdIsGivenThenShouldBeReturnedBadRequest() {
        final Long id = 5L;

        Mockito
                .when(repository.findById(id))
                .thenReturn(Optional.empty());
        Assertions.assertThrows(AuthorNotFoundException.class, () -> service.findById(id));
    }

    @Test
    void whenListAuthorsIsCalledThenItShouldBeReturned() {
        AuthorDTO expectedAuthorDTOReturned = authorDTOBuilder.buildAuthorDTO();

        Mockito
                .when(repository.findAllAsDTO(Sort.by("name")))
                .thenReturn(Collections.singletonList(expectedAuthorDTOReturned));

        List<AuthorDTO> authorsReturned = service.findAll();
        Assertions.assertEquals(1, authorsReturned.size());
        Assertions.assertEquals(expectedAuthorDTOReturned, authorsReturned.get(0));
    }

    @Test
    void whenListAuthorsIsCalledThenAnEmptyListShouldBeReturned() {
        Mockito
                .when(repository.findAllAsDTO(Sort.by("name")))
                .thenReturn(Collections.EMPTY_LIST);

        List<AuthorDTO> authorsReturned = service.findAll();
        Assertions.assertTrue(authorsReturned.isEmpty());
    }

    @Test
    void whenDeleteIsCalledWithValidIdThenItShouldBeDeleted() {
        Author authorToDelete = mapper.toModel(authorDTOBuilder.buildAuthorDTO());

        Mockito
                .when(repository.findById(authorToDelete.getId()))
                .thenReturn(Optional.of(authorToDelete));
        Mockito.doNothing()
                .when(repository).delete(authorToDelete);

        service.delete(authorToDelete.getId());

        Mockito.verify(repository, Mockito.times(1)).findById(authorToDelete.getId());
        Mockito.verify(repository, Mockito.times(1)).delete(authorToDelete);
    }

    @Test
    void whenDeleteIsCalledWithInvalidIdThenThrowAuthorNotFoundException() {
        Author authorToDelete = mapper.toModel(authorDTOBuilder.buildAuthorDTO());
        Long idToDelete = authorToDelete.getId();
        Mockito.when(repository.findById(authorToDelete.getId())).thenReturn(Optional.empty());
        Assertions.assertThrows(AuthorNotFoundException.class, () -> service.delete(idToDelete));
    }
}
