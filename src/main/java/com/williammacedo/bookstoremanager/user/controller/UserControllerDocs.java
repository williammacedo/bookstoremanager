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
}
