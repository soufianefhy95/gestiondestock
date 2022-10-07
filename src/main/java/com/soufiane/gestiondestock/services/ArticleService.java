package com.soufiane.gestiondestock.services;

import com.soufiane.gestiondestock.dto.ArticleDto;
import com.soufiane.gestiondestock.dto.LigneCommandeClientDto;
import com.soufiane.gestiondestock.dto.LigneCommandeFournisseurDto;
import com.soufiane.gestiondestock.dto.LigneVenteDto;

import java.util.List;

public interface ArticleService {

    ArticleDto save(ArticleDto dto);

    ArticleDto findById(Integer id);

    ArticleDto findByCodeArticle(String codeArticle);

    List<ArticleDto> findAll();

    List<LigneVenteDto> findHistoriqueVentes(Integer idArticle);

    List<LigneCommandeClientDto> findHistoriqueCommandeClient(Integer idArticle);

    List<LigneCommandeFournisseurDto> findHistoriqueCommandeFournisseur(Integer idArticle);

    List<ArticleDto> findAllArticleByCategory(Integer idCategory);

    void delete(Integer id);

}
