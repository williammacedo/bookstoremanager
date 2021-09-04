package com.williammacedo.bookstoremanager.publisher.controller;

import com.williammacedo.bookstoremanager.publisher.dto.PublisherDTO;
import com.williammacedo.bookstoremanager.publisher.service.PublisherService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/v1/publishers")
public class PublisherController implements PublisherControllerDocs {

    private PublisherService service;

    @GetMapping("/{id}")
    public PublisherDTO findById(@PathVariable Long id) {
        return service.findById(id);
    }

    @GetMapping
    public List<PublisherDTO> findAll() {
        return service.findAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PublisherDTO create(@RequestBody @Valid PublisherDTO dto) {
        return service.create(dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
