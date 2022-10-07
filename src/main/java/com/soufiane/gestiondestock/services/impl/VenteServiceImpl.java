package com.soufiane.gestiondestock.services.impl;

import com.soufiane.gestiondestock.dto.*;
import com.soufiane.gestiondestock.exception.EntityNotFoundException;
import com.soufiane.gestiondestock.exception.ErrorCodes;
import com.soufiane.gestiondestock.exception.InvalidEntityException;
import com.soufiane.gestiondestock.model.*;
import com.soufiane.gestiondestock.repository.ArticleRepository;
import com.soufiane.gestiondestock.repository.LigneVenteRepository;
import com.soufiane.gestiondestock.repository.VenteRepository;
import com.soufiane.gestiondestock.services.MvtStkService;
import com.soufiane.gestiondestock.services.VenteService;
import com.soufiane.gestiondestock.validator.VenteValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class VenteServiceImpl implements VenteService {

    private ArticleRepository articleRepository;
    private VenteRepository venteRepository;
    private LigneVenteRepository ligneVenteRepository;
    private MvtStkService mvtStkService;

    @Autowired
    public VenteServiceImpl(ArticleRepository articleRepository, VenteRepository venteRepository, LigneVenteRepository ligneVenteRepository, MvtStkService mvtStkService) {
        this.articleRepository = articleRepository;
        this.venteRepository = venteRepository;
        this.ligneVenteRepository = ligneVenteRepository;
        this.mvtStkService = mvtStkService;
    }

    @Override
    public VenteDto save(VenteDto dto) {
        List<String> errors = VenteValidator.validate(dto);
        if (!errors.isEmpty()) {
            log.error("Vente n'est pas valide");
            throw new InvalidEntityException(
                    "Les infos ventes ne sont pas valides",
                    ErrorCodes.VENTE_NOT_VALID,
                    errors
            );
        }

        List<String> articleErrors = new ArrayList<>();

        dto.getLigneVente().forEach(ligneVenteDto -> {
            Optional<Article> article = articleRepository.findById(ligneVenteDto.getArticle().getId());
            if (article.isEmpty()) {
                articleErrors.add("Aucun article avec l'ID : "+ ligneVenteDto.getArticle().getId() + "n'a ete trouve dans la BDD");
            }
        });

        if (!articleErrors.isEmpty()) {
            log.error("One or more articles were not foound in DB, {}", errors);
            throw new InvalidEntityException(
                    "Un ou plusieurs articles n'ont pas ete trouve dans la BDD",
                    ErrorCodes.VENTE_NOT_VALID,
                    errors);
        }

        Vente savedVente = venteRepository.save(VenteDto.toEntity(dto));

        dto.getLigneVente().forEach(ligneVenteDto -> {
            LigneVente ligneVente = LigneVenteDto.toEntity(ligneVenteDto);
            ligneVente.setVente(savedVente);
            ligneVenteRepository.save(ligneVente);
            updateMvtStk(ligneVente);
        });

        return VenteDto.fromEntity(savedVente);
    }

    @Override
    public VenteDto findById(Integer id) {
        if (id == null) {
            log.warn("Vente ID is NULL");
            return null;
        }
        return venteRepository.findById(id)
                .map(VenteDto::fromEntity)
                .orElseThrow(()-> new EntityNotFoundException(
                        "Aucun vente n'a ete trouve dans la BDD",
                        ErrorCodes.VENTE_NOT_FOUND
                ));
    }

    @Override
    public VenteDto findByCode(String code) {
        if (!StringUtils.hasLength(code)) {
            log.warn("Vente ID is NULL");
            return null;
        }
        return venteRepository.findVenteByCode(code)
                .map(VenteDto::fromEntity)
                .orElseThrow(()-> new EntityNotFoundException(
                        "Aucun vente n'a ete trouve dans la BDD avec le CODE : " + code,
                        ErrorCodes.VENTE_NOT_VALID
                ));
    }

    @Override
    public List<VenteDto> findAll() {
        return venteRepository.findAll().stream()
                .map(VenteDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(Integer id) {
        if (id == null) {
            log.error("Vente ID is null");
            return;
        }
        venteRepository.deleteById(id);
    }


    private void updateMvtStk(LigneVente ligneVente) {
       MvtStkDto mvtStkDto = MvtStkDto.builder()
            .article(ArticleDto.fromEntity(ligneVente.getArticle()))
            .dateMvt(Instant.now())
            .typeMvt(TypeMvtStk.SORTIE)
            .sourceMvtStk(SourceMvtStk.VENTE)
            .quantite(ligneVente.getQuantite())
            .idEntreprise(ligneVente.getIdEntreprise())
            .build();

       mvtStkService.sortieStock(mvtStkDto);
    }
}
