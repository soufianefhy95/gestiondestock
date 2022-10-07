package com.soufiane.gestiondestock.controller.api;

import com.soufiane.gestiondestock.dto.CommandeClientDto;
import com.soufiane.gestiondestock.dto.LigneCommandeClientDto;
import com.soufiane.gestiondestock.model.EtatCommande;
import io.swagger.annotations.Api;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

import static com.soufiane.gestiondestock.utils.Constants.APP_ROOT;

@Api(APP_ROOT + "/commandesclients")
public interface CommandeClientApi {

    @PostMapping(value = APP_ROOT + "/commandesclients/create", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<CommandeClientDto> save(@RequestBody CommandeClientDto dto);

    @PatchMapping(value = APP_ROOT + "/commandesclients//update/etat/{idCommande}/{etatCommande}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<CommandeClientDto> updateEtatCommande(@PathVariable("idCommande") Integer idCommande, @PathVariable("etatCommande") EtatCommande etatCommande);

    @PatchMapping(value = APP_ROOT + "/commandesclients/update/quantite/{idCommande}/{idLigneCommande}/{quantite}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<CommandeClientDto> updateQuantiteCommande(@PathVariable("idCommande") Integer idCommande,
                                                             @PathVariable("idLigneCommande") Integer idLigneCommande,
                                                             @PathVariable("quantite") BigDecimal quantite);

    @PatchMapping(value = APP_ROOT + "/commandesclients/update/client/{idCommande}/{idClient}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<CommandeClientDto> updateClient(@PathVariable("idCommande") Integer idCommande,
                                                   @PathVariable("idClient") Integer idClient);

    @PatchMapping(value = APP_ROOT + "/commandesclients/update/article/{idCommande}/{idLigneCommande}/{idArticle}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<CommandeClientDto> updateArticle(@PathVariable("idCommande") Integer idCommande,
                                    @PathVariable("idLigneCommande") Integer idLigneCommande,
                                    @PathVariable("idArticle") Integer newIdArticle);


    @DeleteMapping(value = APP_ROOT + "/commandesclients/delete/article/{idCommande}/{idLigneCommande}")
    ResponseEntity<CommandeClientDto> deleteArticle(@PathVariable("idCommande") Integer idCommande,
                                                    @PathVariable("idLigneCommande") Integer idLigneCommande);

    @GetMapping(value = APP_ROOT + "/commandesclients/{idCommandeClient}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<CommandeClientDto> findById(@PathVariable Integer idCommandeClient);

    @GetMapping(value = APP_ROOT + "/commandesclients/get/{codeCommandeClient}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<CommandeClientDto> findByCode(@PathVariable("codeCommandeClient") String code);

    @GetMapping(value = APP_ROOT + "/commandesclients/all", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<List<CommandeClientDto>> findAll();

    @GetMapping(value = APP_ROOT + "/commandesclients/lignesCommandes/all/{idCommande}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<List<LigneCommandeClientDto>> findAllLignesCommandeClientByCommandeClientId(
            @PathVariable("idCommande") Integer idCommande);

    @DeleteMapping(value = APP_ROOT + "/commandesclients/delete/{idCommandeClient}")
    ResponseEntity delete(@PathVariable("idCommandeClient") Integer id);
}
