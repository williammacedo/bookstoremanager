package com.williammacedo.bookstoremanager.user.controller;

import com.williammacedo.bookstoremanager.book.dto.BookResponseDTO;
import com.williammacedo.bookstoremanager.book.service.BookService;
import com.williammacedo.bookstoremanager.user.dto.AuthenticatedUser;
import com.williammacedo.bookstoremanager.user.dto.UserDTO;
import com.williammacedo.bookstoremanager.user.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("v1/users")
@AllArgsConstructor
public class UserController implements UserControllerDocs {

    private UserService service;
    private BookService bookService;

    @GetMapping
    public List<UserDTO> findAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public UserDTO findById(@PathVariable Long id) {
        return service.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDTO create(@RequestBody @Valid UserDTO dto) {
        return service.create(dto);
    }

    @PutMapping("/{id}")
    public UserDTO update(@PathVariable Long id, @RequestBody @Valid UserDTO dto) {
        return service.update(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }

    @GetMapping("/books")
    public List<BookResponseDTO> getBooks(@ApiIgnore @AuthenticationPrincipal AuthenticatedUser authenticatedUser) {
        return bookService.getBooksByUser(authenticatedUser.getUsername());
    }

    @PostMapping("/books")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void addBooks(@ApiIgnore @AuthenticationPrincipal AuthenticatedUser authenticatedUser, @RequestBody List<Long> ids) {
        service.addBooks(authenticatedUser, ids);
    }
}
