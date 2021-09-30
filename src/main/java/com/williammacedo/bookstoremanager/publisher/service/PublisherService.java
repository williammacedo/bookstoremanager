package com.williammacedo.bookstoremanager.publisher.service;

import com.williammacedo.bookstoremanager.publisher.dto.PublisherDTO;
import com.williammacedo.bookstoremanager.publisher.entity.Publisher;
import com.williammacedo.bookstoremanager.publisher.exception.PublisherAlreadyExistsException;
import com.williammacedo.bookstoremanager.publisher.exception.PublisherNotFoundException;
import com.williammacedo.bookstoremanager.publisher.mapper.PublisherMapper;
import com.williammacedo.bookstoremanager.publisher.repository.PublisherRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class PublisherService {

    private static final PublisherMapper mapper = PublisherMapper.INSTANCE;

    private PublisherRepository repository;

    public PublisherDTO findById(Long id) {
        Publisher publisher = verifyAndGet(id);
        return mapper.toDTO(publisher);
    }

    public List<PublisherDTO> findAll() {
        return repository.findAllAsDTO();
    }

    @Transactional
    public PublisherDTO create(PublisherDTO dto) {
        verifyIfExists(dto.getName(), dto.getCode());
        Publisher publisherToCreate = mapper.toModel(dto);
        Publisher publisherCreated = repository.save(publisherToCreate);
        return mapper.toDTO(publisherCreated);
    }

    @Transactional
    public void delete(Long id) {
        Publisher publisher = verifyAndGet(id);
        repository.delete(publisher);
    }

    private void verifyIfExists(String name, String code) {
        repository.findByNameOrCode(name, code)
            .ifPresent(publisher -> {
                throw new PublisherAlreadyExistsException(name, code);
            });
    }

    public Publisher verifyAndGet(Long id) {
        return repository.findById(id).orElseThrow(() -> new PublisherNotFoundException(id));
    }
}
