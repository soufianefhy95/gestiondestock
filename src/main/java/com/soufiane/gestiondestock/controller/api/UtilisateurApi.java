package com.soufiane.gestiondestock.controller.api;

import com.soufiane.gestiondestock.dto.ChangerMotDePasseUtilisateurDto;
import com.soufiane.gestiondestock.dto.UtilisateurDto;
import io.swagger.annotations.Api;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.soufiane.gestiondestock.utils.Constants.APP_ROOT;
import static com.soufiane.gestiondestock.utils.Constants.UTILISATEUR_ENDPOINT;

@Api(UTILISATEUR_ENDPOINT)
public interface UtilisateurApi {

    @PostMapping(value = UTILISATEUR_ENDPOINT + "/create", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    UtilisateurDto save(@RequestBody UtilisateurDto dto);

    @GetMapping(value = UTILISATEUR_ENDPOINT + "/{idUtilisateur}", produces = MediaType.APPLICATION_JSON_VALUE)
    UtilisateurDto findById(@PathVariable("idUtilisateur") Integer id);

    @GetMapping(value = UTILISATEUR_ENDPOINT + "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    List<UtilisateurDto> findAll();

    @DeleteMapping(value = UTILISATEUR_ENDPOINT + "/delete/{idUtilisateur}")
    void delete(@PathVariable("idUtilisateur") Integer id);

    @PatchMapping(value = UTILISATEUR_ENDPOINT + "/update/motDePasse")
    UtilisateurDto changerMotDePasse(@RequestBody ChangerMotDePasseUtilisateurDto dto);
}
