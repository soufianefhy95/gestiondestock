package com.soufiane.gestiondestock.controller.api;

import com.soufiane.gestiondestock.dto.ClientDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.soufiane.gestiondestock.utils.Constants.APP_ROOT;

@Api(APP_ROOT + "/clients")
public interface ClientApi {

    @PostMapping(value = APP_ROOT + "/clients/create", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Save a client (Add / Modify)", notes = "This methods let you save or modify a client", response = ClientDto.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The client has been created or modified successfully"),
            @ApiResponse(code = 400, message = "This given client id not valid")
    })
    ClientDto save(@RequestBody ClientDto dto);

    @GetMapping(value = APP_ROOT + "/clients/{idClient}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Look for a client by ID", notes = "This methods let you look for a category by its ID", response = ClientDto.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The client is found"),
            @ApiResponse(code = 404, message = "No client has been found for this given ID")
    })
    ClientDto findById(@PathVariable("idClient") Integer id);

    @GetMapping(value = APP_ROOT + "/clients/all", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Get all clients", notes = "This methods send you the list of clients", responseContainer = "List<ClientDto.class>")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "List of client / Empty list"),
    })
    List<ClientDto> findAll();

    @DeleteMapping(value = APP_ROOT + "/clients/delete/{idClient}")
    @ApiOperation(value = "Delete a client by ID", notes = "This methods delete a client by its ID")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The client has been deleted successfully"),
    })
    void delete(@PathVariable("idClient") Integer id);
}
