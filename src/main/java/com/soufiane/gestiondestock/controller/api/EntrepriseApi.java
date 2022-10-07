package com.soufiane.gestiondestock.controller.api;

import com.soufiane.gestiondestock.dto.EntrepriseDto;
import io.swagger.annotations.Api;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.soufiane.gestiondestock.utils.Constants.APP_ROOT;
import static com.soufiane.gestiondestock.utils.Constants.ENTREPRISE_ENDPOINT;

@Api(ENTREPRISE_ENDPOINT)
public interface EntrepriseApi {

    @PostMapping(value = ENTREPRISE_ENDPOINT + "/create", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    EntrepriseDto save(@RequestBody EntrepriseDto dto);

    @GetMapping(value = ENTREPRISE_ENDPOINT + "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    EntrepriseDto findById(@PathVariable("id") Integer id);

    @GetMapping(value = ENTREPRISE_ENDPOINT + "/get/{codeFiscal}", produces = MediaType.APPLICATION_JSON_VALUE)
    EntrepriseDto findByCodeFiscal(@PathVariable("codeFiscal") String codeFiscal);

    @GetMapping(value = ENTREPRISE_ENDPOINT + "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    List<EntrepriseDto> findAll();

    @DeleteMapping(value = ENTREPRISE_ENDPOINT + "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    void delete(@PathVariable("id") Integer id);
}
