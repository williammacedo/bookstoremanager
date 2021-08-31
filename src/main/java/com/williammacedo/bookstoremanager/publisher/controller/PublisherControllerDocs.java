package com.williammacedo.bookstoremanager.publisher.controller;

import com.williammacedo.bookstoremanager.publisher.dto.PublisherDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import java.util.List;

@Api(value = "Publishers management", tags = "PublisherController")
public interface PublisherControllerDocs {
    @ApiOperation(value = "Find Publisher by id operation")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success publisher found"),
            @ApiResponse(code = 404, message = "Publisher not found.")
    })
    PublisherDTO findById(Long id);

    @ApiOperation(value = "List all registered publishers")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success return all publishers"),
    })
    List<PublisherDTO> findAll();

    @ApiOperation(value = "Publisher creation operation")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Success publisher creation"),
            @ApiResponse(code = 400, message = "Missing required fields, wrong field range value or publisher already registered on system.")
    })
    PublisherDTO create(PublisherDTO dto);

    @ApiOperation(value = "Publisher exclusion operation.")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "No content, publisher deleted."),
            @ApiResponse(code = 404, message = "Publisher not found.")
    })
    void delete(Long id);
}
