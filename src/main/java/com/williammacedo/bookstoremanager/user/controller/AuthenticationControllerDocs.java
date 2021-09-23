package com.williammacedo.bookstoremanager.user.controller;

import com.williammacedo.bookstoremanager.user.dto.JwtRequest;
import com.williammacedo.bookstoremanager.user.dto.JwtResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api(value = "Authentication management", tags = "AuthenticationController")
public interface AuthenticationControllerDocs {

    @ApiOperation(value = "User authentication operation.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success user authenticated."),
            @ApiResponse(code = 404, message = "User not found.")
    })
    JwtResponse autenticar(JwtRequest jwtRequest);
}
