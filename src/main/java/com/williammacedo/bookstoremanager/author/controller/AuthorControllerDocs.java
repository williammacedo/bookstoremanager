package com.williammacedo.bookstoremanager.author.controller;

import com.williammacedo.bookstoremanager.author.dto.AuthorDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import java.util.List;

@Api(value = "Authors management", tags = "AuthorController")
public interface AuthorControllerDocs {

    @ApiOperation(value = "Find Author by id operation")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success author found"),
            @ApiResponse(code = 404, message = "Author not found.")
    })
    AuthorDTO findById(Long id);

    @ApiOperation(value = "List all registered authors")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success return all authors"),
    })
    List<AuthorDTO> findAll();

    @ApiOperation(value = "Author creation operation")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Success author creation"),
            @ApiResponse(code = 400, message = "Missing required fields, wrong field range value or author already registered on system.")
    })
    AuthorDTO create(AuthorDTO dto);

    @ApiOperation(value = "Author exclusion operation.")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "No content, author deleted."),
            @ApiResponse(code = 404, message = "Author not found.")
    })
    void delete(Long id);
}
