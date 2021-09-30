package com.williammacedo.bookstoremanager.book.controller;

import com.williammacedo.bookstoremanager.book.dto.BookRequestDTO;
import com.williammacedo.bookstoremanager.book.dto.BookResponseDTO;
import com.williammacedo.bookstoremanager.user.dto.UserDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import java.util.List;

@Api(value = "Books management", tags = "BookController")
public interface BookControllerDocs {

    @ApiOperation(value = "Find Book by id operation")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Success book found"),
        @ApiResponse(code = 404, message = "Book not found.")
    })
    BookResponseDTO findBy(Long id);

    @ApiOperation(value = "List all registered books")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success return all books"),
    })
    List<BookResponseDTO> findAll();

    @ApiOperation(value = "Book creation operation")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Success book creation"),
            @ApiResponse(code = 400, message = "Missing required fields, wrong field range value or book already registered on system"),
            @ApiResponse(code = 404, message = "Author or Publisher not found")
    })
    BookResponseDTO create(BookRequestDTO bookRequestDTO);

    @ApiOperation(value = "Book update operation")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Success update Book"),
            @ApiResponse(code = 400, message = "Missing required fields, wrong field range value or book already registered on system."),
            @ApiResponse(code = 404, message = "Book or Author or Publisher not found ."),
    })
    BookResponseDTO update(Long id, BookRequestDTO bookRequestDTO);

    @ApiOperation(value = "Book exclusion operation.")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "No content, book deleted."),
            @ApiResponse(code = 404, message = "Book not found.")
    })
    void delete(Long id);
}
