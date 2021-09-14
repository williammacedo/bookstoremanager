package com.williammacedo.bookstoremanager.user.controller;

import com.williammacedo.bookstoremanager.user.dto.UserDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import java.util.List;

@Api(value = "Users management", tags = "UserController")
public interface UserControllerDocs {

    @ApiOperation(value = "List all registered users")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success return all users"),
    })
    List<UserDTO> findAll();

    @ApiOperation(value = "Find User by id operation")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success users found"),
            @ApiResponse(code = 404, message = "User not found.")
    })
    UserDTO findById(Long id);

    @ApiOperation(value = "User creation operation")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Success user creation"),
            @ApiResponse(code = 400, message = "Missing required fields, wrong field range value or user already registered on system.")
    })
    UserDTO create(UserDTO dto);

    @ApiOperation(value = "User update operation")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Success update user"),
            @ApiResponse(code = 400, message = "Missing required fields, wrong field range value or user already registered on system."),
            @ApiResponse(code = 404, message = "User not found."),
    })
    UserDTO update(Long id, UserDTO dto);

    @ApiOperation(value = "User exclusion operation.")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "No content, author deleted."),
            @ApiResponse(code = 404, message = "User not found.")
    })
    void delete(Long id);
}
