package com.williammacedo.bookstoremanager.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "BookController")
@RestController
@RequestMapping("/v1/books")
public class BookController {

    @ApiOperation("Return an example hello world")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success method return")
    })
    @GetMapping
    public ResponseEntity<String> hello() {
        return ResponseEntity.ok( "Hello World and I'm running an example with PR, new PR.");
    }
}
