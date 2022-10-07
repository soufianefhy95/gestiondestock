package com.soufiane.gestiondestock.services;

import com.soufiane.gestiondestock.dto.CommandeClientDto;
import com.soufiane.gestiondestock.dto.LigneCommandeClientDto;
import com.soufiane.gestiondestock.model.EtatCommande;
import io.swagger.models.auth.In;

import java.math.BigDecimal;
import java.util.List;

public interface CommandeClientService {

    CommandeClientDto save(CommandeClientDto dto);

    CommandeClientDto updateEtatCommande(Integer idCommande, EtatCommande etatCommande);

    CommandeClientDto updateQuantiteCommande(Integer idCommande, Integer idLigneCommande, BigDecimal quantite);

    CommandeClientDto updateClient(Integer idCommande, Integer idClient);

    CommandeClientDto updateArticle(Integer idCommande, Integer idLigneCommande, Integer newIdArticle);

    // delete an article ==> delete LigneCommandeClient
    CommandeClientDto deleteArticle(Integer idCommande, Integer idLigneCommande);

    CommandeClientDto findById(Integer id);

    CommandeClientDto findByCode(String code);

    List<CommandeClientDto> findAll();


    List<LigneCommandeClientDto> findAllLignesCommandeClientByCommandeClientId(Integer idCommande);

    void delete(Integer id);
}
