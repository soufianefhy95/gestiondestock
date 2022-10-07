package com.soufiane.gestiondestock.controller.api;

import com.soufiane.gestiondestock.dto.CategoryDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.soufiane.gestiondestock.utils.Constants.APP_ROOT;

@Api(APP_ROOT + "/categories")
public interface CategoryApi {

    @PostMapping(value = APP_ROOT + "/categories/create", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Save a category (Add / Modify)", notes = "This methods let you save or modify a category", response = CategoryDto.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The category has been created or modified successfully"),
            @ApiResponse(code = 400, message = "This given category id not valid")
    })
    CategoryDto save(@RequestBody CategoryDto dto);

    @GetMapping(value = APP_ROOT + "/categories/get/{idCategory}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Look for a category by ID", notes = "This methods let you look for a category by its ID", response = CategoryDto.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The category is found"),
            @ApiResponse(code = 404, message = "No category has been found for this given ID")
    })
    CategoryDto findById(@PathVariable("idCategory") Integer idCategory);

    @GetMapping(value = APP_ROOT + "/categories/{codeCategory}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Look for a category by CODE", notes = "This methods let you look for a category by its CODE", response = CategoryDto.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The category is found"),
            @ApiResponse(code = 404, message = "No category has been found for this given CODE")
    })
    CategoryDto findByCode(@PathVariable("codeCategory") String codeCategory);

    @GetMapping(value = APP_ROOT + "/categories/all", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Get all categories", notes = "This methods send you the list of categories", responseContainer = "List<CategoryDto.class>")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "List of category / Empty list"),
    })
    List<CategoryDto> findAll();

    @DeleteMapping(value = APP_ROOT + "/categories/delete/{id}")
    @ApiOperation(value = "Delete a category by ID", notes = "This methods delete a category by its ID")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The category has been deleted successfully"),
    })
    void delete(@PathVariable("id") Integer id);
}
