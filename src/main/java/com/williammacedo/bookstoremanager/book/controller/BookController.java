package com.williammacedo.bookstoremanager.book.controller;

import com.williammacedo.bookstoremanager.book.dto.BookRequestDTO;
import com.williammacedo.bookstoremanager.book.dto.BookResponseDTO;
import com.williammacedo.bookstoremanager.book.service.BookService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("v1/books")
@AllArgsConstructor
public class BookController implements BookControllerDocs{

    private BookService service;

    @GetMapping("/{id}")
    public BookResponseDTO findBy(@PathVariable Long id) {
        return service.getById(id);
    }

    @GetMapping
    public List<BookResponseDTO> findAll() {
        return service.findAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookResponseDTO create(@RequestBody @Valid BookRequestDTO dto) {
        return service.create(dto);
    }

    @PutMapping("/{id}")
    public BookResponseDTO update(@PathVariable Long id, @RequestBody @Valid BookRequestDTO dto) {
        return service.update(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
