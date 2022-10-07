package com.soufiane.gestiondestock.repository;

import com.soufiane.gestiondestock.model.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.query.Param;
//import java.util.List;

public interface ArticleRepository extends JpaRepository<Article, Integer> {

//    @Query("select a from article where codearticle = :code and designation = :designation")
//    List<Article> findByCustomQuery(@Param("code") String c, @Param("designation") String d);
//
//    @Query(value = "select * from article where code = :code", nativeQuery = true)
//    List<Article> findByCustomNativeQuery(@Param("code") String c);
//
//    List<Article> findByCodeArticleIgnoreCaseAndDesignationIgnoreCase(String codeArticle, String designation);
    Optional<Article> findArticleByCodeArticle(String codeArticle);

    List<Article> findAllByCategoryId(Integer idCategory);
}
