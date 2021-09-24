package com.williammacedo.bookstoremanager.book.controller;

import com.williammacedo.bookstoremanager.book.dto.BookResponseDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import java.util.List;

@Api(value = "Books management", tags = "BookController")
public interface BookControllerDocs {

    @ApiOperation(value = "List all registered books")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success return all books"),
    })
    List<BookResponseDTO> findAll();
}
