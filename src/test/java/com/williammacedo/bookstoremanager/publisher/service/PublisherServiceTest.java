package com.williammacedo.bookstoremanager.publisher.service;

import com.williammacedo.bookstoremanager.publisher.builder.PublisherDTOBuilder;
import com.williammacedo.bookstoremanager.publisher.dto.PublisherDTO;
import com.williammacedo.bookstoremanager.publisher.entity.Publisher;
import com.williammacedo.bookstoremanager.publisher.exception.PublisherAlreadyExistsException;
import com.williammacedo.bookstoremanager.publisher.exception.PublisherNotFoundException;
import com.williammacedo.bookstoremanager.publisher.mapper.PublisherMapper;
import com.williammacedo.bookstoremanager.publisher.repository.PublisherRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
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
public class PublisherServiceTest {

    PublisherMapper mapper = PublisherMapper.INSTANCE;

    @Mock
    PublisherRepository repository;

    @InjectMocks
    PublisherService service;

    private PublisherDTOBuilder dtoBuilder;

    @BeforeEach
    void setUp() {
        dtoBuilder = PublisherDTOBuilder.builder().build();
    }

    @Test
    @DisplayName("Unit test - List all publishers")
    void whenListPublisherIsCalledThenItShouldBeReturned() {
        PublisherDTO expectedPublisherDTOReturned = dtoBuilder.buildPublisherDTO();
        Mockito.when(repository.findAllAsDTO()).thenReturn(Collections.singletonList(expectedPublisherDTOReturned));

        List<PublisherDTO> publisherReturned = service.findAll();
        Assertions.assertNotNull(publisherReturned);
        Assertions.assertFalse(publisherReturned.isEmpty());
        Assertions.assertEquals(1, publisherReturned.size());
        Assertions.assertEquals(expectedPublisherDTOReturned, publisherReturned.get(0));
    }

    @Test
    @DisplayName("Unit test - EmptyList publishers should returned")
    void whenListPublisherIsCalledThenAnEmptyListShouldBeReturned() {
        Mockito.when(repository.findAllAsDTO()).thenReturn(Collections.EMPTY_LIST);

        List<PublisherDTO> publisherReturned = service.findAll();
        Assertions.assertNotNull(publisherReturned);
        Assertions.assertTrue(publisherReturned.isEmpty());
    }

    @Test
    @DisplayName("Unit test - Get publisher by ID")
    void whenGETPublisherIsCalledThenItShouldBeReturned() {
        PublisherDTO expectedPublisherDTOReturned = dtoBuilder.buildPublisherDTO();
        Publisher expectedPublisher = mapper.toModel(expectedPublisherDTOReturned);
        Mockito.when(repository.findById(expectedPublisher.getId())).thenReturn(Optional.of(expectedPublisher));

        PublisherDTO publisherDTOReturned = service.findById(expectedPublisher.getId());
        Assertions.assertNotNull(publisherDTOReturned);
        Assertions.assertEquals(expectedPublisherDTOReturned, publisherDTOReturned);
    }

    @Test
    @DisplayName("Unit test - Get publisher by Invalid ID 404")
    void whenGETPublisherIsCalledWithInvalidIDThenNotFound() {
        Long publisherId = 1L;
        Mockito.when(repository.findById(publisherId)).thenReturn(Optional.empty());

        Assertions.assertThrows(PublisherNotFoundException.class, () -> service.findById(publisherId));
    }

    @Test
    @DisplayName("Unit test - DELETE publisher by -> ID")
    void whenDELETEPublisherIsCalledThenItDelete() {
        PublisherDTO expectedPublisherDTOReturned = dtoBuilder.buildPublisherDTO();
        Publisher expectedPublisher = mapper.toModel(expectedPublisherDTOReturned);
        Mockito.when(repository.findById(expectedPublisher.getId())).thenReturn(Optional.of(expectedPublisher));
        Mockito.doNothing().when(repository).delete(expectedPublisher);

        service.delete(expectedPublisher.getId());
        Mockito.verify(repository, Mockito.times(1)).findById(expectedPublisher.getId());
        Mockito.verify(repository, Mockito.times(1)).delete(expectedPublisher);
    }

    @Test
    @DisplayName("Unit test - DELETE publisher by Invalid ID -> 404")
    void whenDELETEPublisherIsCalledWithInvalidIDThenNotFound() {
        Long publisherId = 1L;
        Mockito.when(repository.findById(publisherId)).thenReturn(Optional.empty());

        Assertions.assertThrows(PublisherNotFoundException.class, () -> service.delete(publisherId));
    }

    @Test
    @DisplayName("Unit test - DELETE publisher by -> ID")
    void whenPOSTPublisherIsCalledThenCreateIt() {
        PublisherDTO expectedPublisherDTOReturned = dtoBuilder.buildPublisherDTO();
        Publisher expectedPublisher = mapper.toModel(expectedPublisherDTOReturned);

        Mockito.when(
                repository.findByNameOrCode(expectedPublisherDTOReturned.getName(), expectedPublisherDTOReturned.getCode())
        ).thenReturn(Optional.empty());
        Mockito.when(repository.save(expectedPublisher)).thenReturn(expectedPublisher);

        PublisherDTO publisherDTOReturned = service.create(expectedPublisherDTOReturned);
        Assertions.assertNotNull(publisherDTOReturned);
        Assertions.assertEquals(expectedPublisherDTOReturned, publisherDTOReturned);
    }

    @Test
    @DisplayName("Unit test - POST publisher With name exists -> 400")
    void whenPOSTPublisherIsCalledWithNameOrCodeAlreadyExistsThenBadRequest() {
        PublisherDTO expectedPublisherDTOReturned = dtoBuilder.buildPublisherDTO();
        Publisher expectedPublisher = mapper.toModel(expectedPublisherDTOReturned);

        Mockito.when(
            repository.findByNameOrCode(expectedPublisherDTOReturned.getName(), expectedPublisherDTOReturned.getCode())
        ).thenReturn(Optional.of(expectedPublisher));

        Assertions.assertThrows(PublisherAlreadyExistsException.class, () -> service.create(expectedPublisherDTOReturned));
    }
}
