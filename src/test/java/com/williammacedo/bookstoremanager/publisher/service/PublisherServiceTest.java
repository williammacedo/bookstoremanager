package com.williammacedo.bookstoremanager.publisher.service;

import com.williammacedo.bookstoremanager.publisher.builder.PublisherDTOBuilder;
import com.williammacedo.bookstoremanager.publisher.dto.PublisherDTO;
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
}
