package com.soufiane.gestiondestock.services.impl;

import com.soufiane.gestiondestock.dto.MvtStkDto;
import com.soufiane.gestiondestock.exception.EntityNotFoundException;
import com.soufiane.gestiondestock.exception.ErrorCodes;
import com.soufiane.gestiondestock.exception.InvalidEntityException;
import com.soufiane.gestiondestock.model.Article;
import com.soufiane.gestiondestock.model.TypeMvtStk;
import com.soufiane.gestiondestock.repository.ArticleRepository;
import com.soufiane.gestiondestock.repository.MvtStkRepository;
import com.soufiane.gestiondestock.services.ArticleService;
import com.soufiane.gestiondestock.services.MvtStkService;
import com.soufiane.gestiondestock.validator.MvtStkValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class MvtStkServiceImpl implements MvtStkService {

    private MvtStkRepository repository;
    private ArticleService articleService;

    @Autowired
    public MvtStkServiceImpl(MvtStkRepository repository, ArticleService articleService) {
        this.repository = repository;
        this.articleService = articleService;
    }

    @Override
    public BigDecimal stockReelArticle(Integer idArticle) {
        if (idArticle == null) {
            log.warn("ID article is NULL");
            return BigDecimal.valueOf(-1);
        }
        articleService.findById(idArticle);
        return repository.stockReelArticle(idArticle);
    }

    @Override
    public List<MvtStkDto> mvtStkArticle(Integer idArticle) {
        return repository.findAllByArticleId(idArticle).stream()
                .map(MvtStkDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public MvtStkDto entreeStock(MvtStkDto mvtStkDto) {
        return entreePositive(mvtStkDto, TypeMvtStk.ENTREE);
    }

    @Override
    public MvtStkDto sortieStock(MvtStkDto mvtStkDto) {
        return sortieNegative(mvtStkDto, TypeMvtStk.SORTIE);
    }

    @Override
    public MvtStkDto correctionStockPos(MvtStkDto mvtStkDto) {
        return entreePositive(mvtStkDto, TypeMvtStk.CORRECTION_POS);
    }

    @Override
    public MvtStkDto correctionStockNeg(MvtStkDto mvtStkDto) {
        return sortieNegative(mvtStkDto, TypeMvtStk.CORRECTION_NEG);
    }

    private MvtStkDto entreePositive(MvtStkDto mvtStkDto, TypeMvtStk typeMvtStk) {
        List<String> errors = MvtStkValidator.validate(mvtStkDto);
        if (!errors.isEmpty()) {
            log.error("Mvt stock is not valid {}", errors);
            throw new InvalidEntityException(
                    "Le mouvement du stock n'est pas valide",
                    ErrorCodes.MVT_STK_NOT_VALID,
                    errors);
        }
        mvtStkDto.setQuantite(
                BigDecimal.valueOf(
                        Math.abs(mvtStkDto.getQuantite().doubleValue()) * -1
                )
        );
        mvtStkDto.setTypeMvt(typeMvtStk);
        return MvtStkDto.fromEntity(
                repository.save(
                        MvtStkDto.toEntity(mvtStkDto)
                )
        );
    }

    private MvtStkDto sortieNegative(MvtStkDto mvtStkDto, TypeMvtStk typeMvtStk) {
        List<String> errors = MvtStkValidator.validate(mvtStkDto);
        if (!errors.isEmpty()) {
            log.error("Mvt stock is not valid {}", errors);
            throw new InvalidEntityException(
                    "Le mouvement du stock n'est pas valide",
                    ErrorCodes.MVT_STK_NOT_VALID,
                    errors);
        }
        mvtStkDto.setQuantite(
                BigDecimal.valueOf(
                        Math.abs(mvtStkDto.getQuantite().doubleValue()) * -1
                )
        );
        mvtStkDto.setTypeMvt(typeMvtStk);
        return MvtStkDto.fromEntity(
                repository.save(
                        MvtStkDto.toEntity(mvtStkDto)
                )
        );
    }
//
//    private MvtStkRepository mvtStkRepository;
//    private ArticleRepository articleRepository;
//
//    @Autowired
//    public MvtStkServiceImpl(MvtStkRepository mvtStkRepository, ArticleRepository articleRepository) {
//        this.mvtStkRepository = mvtStkRepository;
//        this.articleRepository = articleRepository;
//    }
//
//    @Override
//    public MvtStkDto save(MvtStkDto dto) {
//        List<String> errors = MvtStkValidator.validate(dto);
//        if (!errors.isEmpty()) {
//            log.error("Mvt stock is not valid {}", dto);
//            throw new InvalidEntityException(
//                    "Les infos du mouvement stock ne sont pas valides",
//                    ErrorCodes.MVT_STK_NOT_VALID,
//                    errors);
//        }
//
//        List<String> articleErrors = new ArrayList<>();
//
//        if (dto.getArticle() != null) {
//            Optional<Article> article = articleRepository.findById(dto.getArticle().getId());
//            if (article.isEmpty()) {
//                articleErrors.add("L'article avec l'ID : "+ dto.getArticle().getId() +" n'existe pas");
//            }
//        } else {
//            articleErrors.add("Impossible d'enregistrer un mouvement de stock avec un article NULL");
//        }
//
//        if (!articleErrors.isEmpty()) {
//            log.warn("Le mouvement stock ne peut pas etre enregistree");
//            throw new InvalidEntityException(
//                    "Un ou plusieurs articles que vous avez fourni n'existent pas",
//                    ErrorCodes.ARTICLE_NOT_FOUND,
//                    articleErrors);
//        }
//
//        return MvtStkDto.fromEntity(
//                mvtStkRepository.save(
//                        MvtStkDto.toEntity(dto)
//                )
//        );
//    }
//
//    @Override
//    public MvtStkDto findById(Integer id) {
//        if (id == null) {
//            log.error("Mouvement stock ID is NULL");
//            return null;
//        }
//        return mvtStkRepository.findById(id)
//                .map(MvtStkDto::fromEntity)
//                .orElseThrow(()-> new EntityNotFoundException(
//                        "Aucun mouvement de stock n'a ete trouvee avec le 'ID : " + id,
//                        ErrorCodes.MVT_STK_NOT_FOUND
//                ));
//    }
//
//    @Override
//    public List<MvtStkDto> findAll() {
//        return mvtStkRepository.findAll().stream()
//                .map(MvtStkDto::fromEntity)
//                .collect(Collectors.toList());
//    }
//
//    @Override
//    public void delete(Integer id) {
//        if (id == null) {
//            log.error("Mouvement de stock ID is null");
//            return;
//        }
//        mvtStkRepository.deleteById(id);
//    }
}
