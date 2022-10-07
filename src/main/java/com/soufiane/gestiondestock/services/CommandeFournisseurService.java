package com.soufiane.gestiondestock.services;

import com.soufiane.gestiondestock.dto.CommandeFournisseurDto;
import com.soufiane.gestiondestock.dto.LigneCommandeFournisseurDto;
import com.soufiane.gestiondestock.model.EtatCommande;

import java.math.BigDecimal;
import java.util.List;

public interface CommandeFournisseurService {

    CommandeFournisseurDto save(CommandeFournisseurDto dto);

    CommandeFournisseurDto findById(Integer id);

    CommandeFournisseurDto findByCode(String code);


    CommandeFournisseurDto updateEtatCommande(Integer idCommande, EtatCommande etatCommande);


    CommandeFournisseurDto updateQuantiteCommande(Integer idCommande, Integer idLigneCommande, BigDecimal quantite);


    CommandeFournisseurDto updateFournisseur(Integer idCommande, Integer idFournisseur);


    CommandeFournisseurDto updateArticle(Integer idCommande, Integer idLigneCommande, Integer newIdArticle);

    // delete an article ==> delete LigneCommandeFournisseur
    CommandeFournisseurDto deleteArticle(Integer idCommande, Integer idLigneCommande);

    List<CommandeFournisseurDto> findAll();

    List<LigneCommandeFournisseurDto> findAllLignesCommandeFournisseurByCommandeFournisseurId(Integer idCommande);

    void delete(Integer id);
}
