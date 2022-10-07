package com.soufiane.gestiondestock.controller.api;

import com.soufiane.gestiondestock.dto.ArticleDto;
import com.soufiane.gestiondestock.dto.LigneCommandeClientDto;
import com.soufiane.gestiondestock.dto.LigneCommandeFournisseurDto;
import com.soufiane.gestiondestock.dto.LigneVenteDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.soufiane.gestiondestock.utils.Constants.APP_ROOT;

@Api(APP_ROOT + "/articles")
public interface ArticleApi {

    @PostMapping(value = APP_ROOT + "/articles/create", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Save an article (Add / Modify)", notes = "This methods let you save or modify an article", response = ArticleDto.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The article has been created or modified successfully"),
            @ApiResponse(code = 400, message = "This given article id not valid")
    })
    ArticleDto save(@RequestBody ArticleDto dto);

    @GetMapping(value = APP_ROOT + "/articles/{idArticle}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Look for an article by ID", notes = "This methods let you look for an article by its ID", response = ArticleDto.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The article is found"),
            @ApiResponse(code = 404, message = "No article has been found for this given ID")
    })
    ArticleDto findById(@PathVariable("idArticle") Integer id);

    @GetMapping(value = APP_ROOT + "/articles/get/{codeArticle}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Look for an article by CODE", notes = "This methods let you look for an article by its CODE", response = ArticleDto.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The article is found"),
            @ApiResponse(code = 404, message = "No article has been found for this given CODE")
    })
    ArticleDto findByCodeArticle(@PathVariable("codeArticle") String codeArticle);

    @GetMapping(value = APP_ROOT + "/articles/all", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Get all articles", notes = "This methods send you the list of articles", responseContainer = "List<ArticleDto.class>")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "List of article / Empty list"),
    })
    List<ArticleDto> findAll();

    @GetMapping(value = APP_ROOT + "/articles/historique/vente/{idArticle}", produces = MediaType.APPLICATION_JSON_VALUE)
    List<LigneVenteDto> findHistoriqueVentes(@PathVariable("idArticle") Integer idArticle);

    @GetMapping(value = APP_ROOT + "/articles/historique/commandeClient/{idArticle}", produces = MediaType.APPLICATION_JSON_VALUE)
    List<LigneCommandeClientDto> findHistoriqueCommandeClient(@PathVariable("idArticle") Integer idArticle);

    @GetMapping(value = APP_ROOT + "/articles/historique/commandeFournisseur/{idArticle}", produces = MediaType.APPLICATION_JSON_VALUE)
    List<LigneCommandeFournisseurDto> findHistoriqueCommandeFournisseur(@PathVariable("idArticle") Integer idArticle);

    @GetMapping(value = APP_ROOT + "/articles/filter/category/{idCategory}", produces = MediaType.APPLICATION_JSON_VALUE)
    List<ArticleDto> findAllArticleByCategory(@PathVariable("idCategory") Integer idCategory);

    @DeleteMapping(value = APP_ROOT + "/articles/delete/{idArticle}")
    @ApiOperation(value = "Delete an article by ID", notes = "This methods delete an article by its ID")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The article has been deleted successfully"),
    })
    void delete(@PathVariable("idArticle") Integer id);
}
