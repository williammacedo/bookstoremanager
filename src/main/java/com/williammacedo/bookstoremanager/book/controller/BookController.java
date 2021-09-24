package com.williammacedo.bookstoremanager.book.controller;

import com.williammacedo.bookstoremanager.book.dto.BookResponseDTO;
import com.williammacedo.bookstoremanager.book.service.BookService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("v1/books")
@AllArgsConstructor
public class BookController implements BookControllerDocs{

    private BookService service;

    @GetMapping
    public List<BookResponseDTO> findAll() {
        return service.findAll();
    }
}
