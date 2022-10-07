package com.soufiane.gestiondestock.controller.api;

import com.soufiane.gestiondestock.dto.CommandeFournisseurDto;
import com.soufiane.gestiondestock.dto.LigneCommandeFournisseurDto;
import com.soufiane.gestiondestock.model.EtatCommande;
import io.swagger.annotations.Api;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

import static com.soufiane.gestiondestock.utils.Constants.*;

@Api(COMMANDE_FOURNISSEUR_ENDPOINT)
public interface CommandeFournisseurApi {

    @PostMapping(value = CREATE_COMMANDE_FOURNISSEUR_ENDPOINT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    CommandeFournisseurDto save(@RequestBody CommandeFournisseurDto dto);

    @GetMapping(value = FIND_COMMANDE_FOURNISSEUR_BY_ID_ENDPOINT, produces = MediaType.APPLICATION_JSON_VALUE)
    CommandeFournisseurDto findById(@PathVariable("idCommandeFournisseur") Integer id);

    @GetMapping(value = FIND_COMMANDE_FOURNISSEUR_BY_CODE_ENDPOINT, produces = MediaType.APPLICATION_JSON_VALUE)
    CommandeFournisseurDto findByCode(@PathVariable("codeCommandeFournisseur") String code);

    @PatchMapping(value = COMMANDE_FOURNISSEUR_ENDPOINT + "/update/etat/{idCommande}/{etatCommande}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    CommandeFournisseurDto updateEtatCommande(@PathVariable("idCommande") Integer idCommande, @PathVariable("etatCommande") EtatCommande etatCommande);

    @PatchMapping(value = COMMANDE_FOURNISSEUR_ENDPOINT + "/update/quantite/{idCommande}/{idLigneCommande}/{quantite}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    CommandeFournisseurDto updateQuantiteCommande(@PathVariable("idCommande") Integer idCommande,
                                                             @PathVariable("idLigneCommande") Integer idLigneCommande,
                                                             @PathVariable("quantite") BigDecimal quantite);

    @PatchMapping(value = COMMANDE_FOURNISSEUR_ENDPOINT + "/update/fournisseur/{idCommande}/{idFournisseur}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    CommandeFournisseurDto updateFournisseur(@PathVariable("idCommande") Integer idCommande,
                                                   @PathVariable("idFournisseur") Integer idFournisseur);

    @PatchMapping(value = COMMANDE_FOURNISSEUR_ENDPOINT + "/update/article/{idCommande}/{idLigneCommande}/{idArticle}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    CommandeFournisseurDto updateArticle(@PathVariable("idCommande") Integer idCommande,
                                                    @PathVariable("idLigneCommande") Integer idLigneCommande,
                                                    @PathVariable("idArticle") Integer newIdArticle);


    @DeleteMapping(value = COMMANDE_FOURNISSEUR_ENDPOINT + "/delete/article/{idCommande}/{idLigneCommande}")
    CommandeFournisseurDto deleteArticle(@PathVariable("idCommande") Integer idCommande,
                                                    @PathVariable("idLigneCommande") Integer idLigneCommande);


    @GetMapping(value = FIND_ALL_COMMANDE_FOURNISSEUR, produces = MediaType.APPLICATION_JSON_VALUE)
    List<CommandeFournisseurDto> findAll();

    @GetMapping(value = COMMANDE_FOURNISSEUR_ENDPOINT + "/lignesCommandes/all/{idCommande}", produces = MediaType.APPLICATION_JSON_VALUE)
    List<LigneCommandeFournisseurDto> findAllLignesCommandeFournisseurByCommandeFournisseurId(
            @PathVariable("idCommande") Integer idCommande);

    @DeleteMapping(value = DELETE_COMMANDE_FOURNISSEUR_BY_ID)
    void delete(@PathVariable("idCommandeFournisseur") Integer id);
}
