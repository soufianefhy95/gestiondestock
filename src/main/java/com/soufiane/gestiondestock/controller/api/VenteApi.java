package com.soufiane.gestiondestock.controller.api;

import com.soufiane.gestiondestock.dto.VenteDto;
import io.swagger.annotations.Api;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.soufiane.gestiondestock.utils.Constants.VENTE_ENDPOINT;

@Api(VENTE_ENDPOINT)
public interface VenteApi {

    @PostMapping(value = VENTE_ENDPOINT + "/create", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    VenteDto save(@RequestBody VenteDto dto);

    @GetMapping(value = VENTE_ENDPOINT + "/{idVente}", produces = MediaType.APPLICATION_JSON_VALUE)
    VenteDto findById(@PathVariable("idVente") Integer id);

    @GetMapping(value = VENTE_ENDPOINT + "/get/{codeVente}", produces = MediaType.APPLICATION_JSON_VALUE)
    VenteDto findByCode(@PathVariable("codeVente") String code);

    @GetMapping(value = VENTE_ENDPOINT + "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    List<VenteDto> findAll();

    @DeleteMapping(value = VENTE_ENDPOINT + "/delete/{idVente}")
    void delete(@PathVariable("idVente") Integer id);
}
