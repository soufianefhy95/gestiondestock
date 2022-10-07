package com.soufiane.gestiondestock.controller;

import com.soufiane.gestiondestock.controller.api.ArticleApi;
import com.soufiane.gestiondestock.dto.ArticleDto;
import com.soufiane.gestiondestock.dto.LigneCommandeClientDto;
import com.soufiane.gestiondestock.dto.LigneCommandeFournisseurDto;
import com.soufiane.gestiondestock.dto.LigneVenteDto;
import com.soufiane.gestiondestock.services.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ArticleController implements ArticleApi {

    private ArticleService articleService;

    @Autowired
    public ArticleController(
            ArticleService articleService
    ) {
        this.articleService = articleService;
    }

    @Override
    public ArticleDto save(ArticleDto dto) {
        return articleService.save(dto);
    }

    @Override
    public ArticleDto findById(Integer id) {
        return articleService.findById(id);
    }

    @Override
    public ArticleDto findByCodeArticle(String codeArticle) {
        return articleService.findByCodeArticle(codeArticle);
    }

    @Override
    public List<ArticleDto> findAll() {
        return articleService.findAll();
    }

    @Override
    public List<LigneVenteDto> findHistoriqueVentes(Integer idArticle) {
        return articleService.findHistoriqueVentes(idArticle);
    }

    @Override
    public List<LigneCommandeClientDto> findHistoriqueCommandeClient(Integer idArticle) {
        return articleService.findHistoriqueCommandeClient(idArticle);
    }

    @Override
    public List<LigneCommandeFournisseurDto> findHistoriqueCommandeFournisseur(Integer idArticle) {
        return articleService.findHistoriqueCommandeFournisseur(idArticle);
    }

    @Override
    public List<ArticleDto> findAllArticleByCategory(Integer idCategory) {
        return articleService.findAllArticleByCategory(idCategory);
    }

    @Override
    public void delete(Integer id) {
        articleService.delete(id);
    }
}
