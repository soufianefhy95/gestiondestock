package com.soufiane.gestiondestock.services.impl;

import com.soufiane.gestiondestock.dto.*;
import com.soufiane.gestiondestock.exception.EntityNotFoundException;
import com.soufiane.gestiondestock.exception.ErrorCodes;
import com.soufiane.gestiondestock.exception.InvalidEntityException;
import com.soufiane.gestiondestock.exception.InvalidOperationException;
import com.soufiane.gestiondestock.model.*;
import com.soufiane.gestiondestock.repository.*;
import com.soufiane.gestiondestock.services.CommandeFournisseurService;
import com.soufiane.gestiondestock.services.MvtStkService;
import com.soufiane.gestiondestock.validator.ArticleValidator;
import com.soufiane.gestiondestock.validator.CommandeFournisseurValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CommandeFournisseurServiceImpl implements CommandeFournisseurService {

    private CommandeFournisseurRepository commandeFournisseurRepository;
    private LigneCommandeFournisseurRepository ligneCommandeFournisseurRepository;
    private FournisseurRepository fournisseurRepository;
    private ArticleRepository articleRepository;
    private MvtStkService mvtStkService;

    public CommandeFournisseurServiceImpl(CommandeFournisseurRepository commandeFournisseurRepository, LigneCommandeFournisseurRepository ligneCommandeFournisseurRepository, FournisseurRepository fournisseurRepository, ArticleRepository articleRepository, MvtStkService mvtStkService) {
        this.commandeFournisseurRepository = commandeFournisseurRepository;
        this.ligneCommandeFournisseurRepository = ligneCommandeFournisseurRepository;
        this.fournisseurRepository = fournisseurRepository;
        this.articleRepository = articleRepository;
        this.mvtStkService = mvtStkService;
    }

    @Override
    public CommandeFournisseurDto save(CommandeFournisseurDto dto) {

        List<String> errors = CommandeFournisseurValidator.validate(dto);

        if (!errors.isEmpty()) {
            log.error("Commande fournisseur n'est pas valide");
            throw new InvalidEntityException(
                    "La commande fournisseur n'est pas valide",
                    ErrorCodes.COMMANDE_FOURNISSEUR_NOT_VALID,
                    errors
            );
        }

        Optional<Fournisseur> fournisseur = fournisseurRepository.findById(dto.getFournisseur().getId());
        if (fournisseur.isEmpty()) {
            log.warn("Fournisseur with ID {} was not found in DB", dto.getFournisseur().getId());
            throw new EntityNotFoundException(
                    "Aucun fournisseur avec l'ID : " + dto.getFournisseur().getId() + " n'a ete trouve dans la DB",
                    ErrorCodes.FOURNISSEUR_NOT_FOUND
            );
        }

        List<String> articleErrors = new ArrayList<>();

        if (dto.getLigneCommandeFournisseurs() != null) {
            dto.getLigneCommandeFournisseurs().forEach(ligneCdeFournisseur -> {
                if (ligneCdeFournisseur.getArticle() != null) {
                    Optional<Article> article = articleRepository.findById(ligneCdeFournisseur.getArticle().getId());
                    if (article.isEmpty()) {
                        articleErrors.add("L'article avec l'ID : "+ ligneCdeFournisseur.getArticle().getId() +" n'existe pas");
                    }
                } else {
                    articleErrors.add("Impossible d'enregistrer une commande avec un article NULL");
                }
            });
        }

        if (!articleErrors.isEmpty()) {
            log.warn("La commande fournisseur ne peut pas etre enregistree");
            throw new InvalidEntityException("Un ou plusieurs articles que vous avez fourni n'existent pas", ErrorCodes.ARTICLE_NOT_FOUND, articleErrors);
        }

        // Saving 'commande fournisseur'
        CommandeFournisseur savedCmdFournisseur = commandeFournisseurRepository.save(CommandeFournisseurDto.toEntity(dto));

        if (dto.getLigneCommandeFournisseurs() != null) {
            // Saving 'ligne commande fournisseurs'
            dto.getLigneCommandeFournisseurs().forEach(ligneCmdFournisseur -> {
                LigneCommandeFournisseur ligneCommandeFournisseur = LigneCommandeFournisseurDto.toEntity(ligneCmdFournisseur);
                ligneCommandeFournisseur.setCommandeFournisseur(savedCmdFournisseur);
                ligneCommandeFournisseurRepository.save(ligneCommandeFournisseur);
            });
        }
        return CommandeFournisseurDto.fromEntity(savedCmdFournisseur);
    }

    @Override
    public CommandeFournisseurDto findById(Integer id) {
        if (id == null) {
            log.error("Commande fournisseur ID is NULL");
            return null;
        }
        return commandeFournisseurRepository.findById(id)
                .map(CommandeFournisseurDto::fromEntity)
                .orElseThrow(()-> new EntityNotFoundException(
                        "Aucune commande fournisseur n'a ete trouvee avec le 'ID : " + id,
                        ErrorCodes.COMMANDE_FOURNISSEUR_NOT_FOUND
                ));
    }

    @Override
    public CommandeFournisseurDto findByCode(String code) {
        if (!StringUtils.hasLength(code)) {
            log.error("Commande fournisseur CODE is NULL");
            return null;
        }
        return commandeFournisseurRepository.findCommandeFournisseurByCode(code)
                .map(CommandeFournisseurDto::fromEntity)
                .orElseThrow(()-> new EntityNotFoundException(
                        "Aucune commande fournisseur n'a ete trouvee avec le code : " + code,
                        ErrorCodes.COMMANDE_FOURNISSEUR_NOT_FOUND
                ));
    }

    @Override
    public CommandeFournisseurDto updateEtatCommande(Integer idCommande, EtatCommande etatCommande) {
        checkIdCommande(idCommande);

        if (!StringUtils.hasLength(String.valueOf(etatCommande))) {
            log.error("L'état de la commande fournisseur is NULL");
            throw new InvalidOperationException(
                    "Impossible de modifier l'état de la commande avec un etat null",
                    ErrorCodes.COMMANDE_FOURNISSEUR_NON_MODIFIABLE
            );
        }

        CommandeFournisseurDto commandeFournisseur = checkEtatCommande(idCommande);
        commandeFournisseur.setEtatCommande(etatCommande);

        CommandeFournisseur savedCmdFournisseur = commandeFournisseurRepository.save(CommandeFournisseurDto.toEntity(commandeFournisseur));
        if (commandeFournisseur.isCommandeLivree()) {
            updateMvtStk(idCommande);
        }
        return CommandeFournisseurDto.fromEntity(savedCmdFournisseur);
    }

    @Override
    public CommandeFournisseurDto updateQuantiteCommande(Integer idCommande, Integer idLigneCommande, BigDecimal quantite) {
        checkIdCommande(idCommande);
        checkIdLigneCommande(idLigneCommande);

        if (quantite == null || quantite.compareTo(BigDecimal.ZERO) == 0) {
            log.error("L'ID de la ligne de commande fournisseur is NULL");
            throw new InvalidOperationException(
                    "Impossible de modifier l'état de la commande avec une quantite null ou ZERO",
                    ErrorCodes.COMMANDE_FOURNISSEUR_NON_MODIFIABLE
            );
        }

        CommandeFournisseurDto commandeFournisseur = checkEtatCommande(idCommande);
        Optional<LigneCommandeFournisseur> ligneCommandeFournisseurOptional = findLigneCommandeFournisseur(idLigneCommande);

        LigneCommandeFournisseur ligneCommandeFournisseur = ligneCommandeFournisseurOptional.get();
        ligneCommandeFournisseur.setQuantite(quantite);
        ligneCommandeFournisseurRepository.save(ligneCommandeFournisseur);

        return commandeFournisseur;
    }

    @Override
    public CommandeFournisseurDto updateFournisseur(Integer idCommande, Integer idFournisseur) {
        checkIdCommande(idCommande);
        if (idFournisseur == null) {
            log.error("L'ID du fournisseur de la commande fournisseur is NULL");
            throw new InvalidOperationException(
                    "Impossible de modifier l'état de la commande fournisseur avec un ID de fournisseur null",
                    ErrorCodes.COMMANDE_FOURNISSEUR_NON_MODIFIABLE
            );
        }

        CommandeFournisseurDto commandeFournisseur = checkEtatCommande(idCommande);

        Optional<Fournisseur> fournisseurOptional = fournisseurRepository.findById(idFournisseur);
        if (fournisseurOptional.isEmpty()) {
            throw new EntityNotFoundException(
                    "Aucun fournisseur n'a été trouvé avec l'ID " + idFournisseur,
                    ErrorCodes.FOURNISSEUR_NOT_FOUND
            );
        }

        commandeFournisseur.setFournisseur(FournisseurDto.fromEntity(fournisseurOptional.get()));

        return CommandeFournisseurDto.fromEntity(
                commandeFournisseurRepository.save(
                        CommandeFournisseurDto.toEntity(commandeFournisseur)
                )
        );
    }

    @Override
    public CommandeFournisseurDto updateArticle(Integer idCommande, Integer idLigneCommande, Integer newIdArticle) {
        checkIdCommande(idCommande);
        checkIdLigneCommande(idLigneCommande);
        checkIdArticle(newIdArticle, "nouvel");

        CommandeFournisseurDto commandeFournisseur = checkEtatCommande(idCommande);
        Optional<LigneCommandeFournisseur> ligneCommandeFournisseur = findLigneCommandeFournisseur(idLigneCommande);

        Optional<Article> articleOptional = articleRepository.findById(newIdArticle);
        if (articleOptional.isEmpty()) {
            throw new EntityNotFoundException(
                    "Aucun article n'a ete trouve pour l'ID : "+ newIdArticle,
                    ErrorCodes.ARTICLE_NOT_FOUND
            );
        }

        List<String> errors = ArticleValidator.validate(ArticleDto.fromEntity(articleOptional.get()));
        if (!errors.isEmpty()) {
            throw new InvalidEntityException(
                    "Article invalid", ErrorCodes.ARTICLE_NOT_VALID, errors
            );
        }

        LigneCommandeFournisseur ligneCommandeFournisseurToSaved = ligneCommandeFournisseur.get();
        ligneCommandeFournisseurToSaved.setArticle(articleOptional.get());
        ligneCommandeFournisseurRepository.save(ligneCommandeFournisseurToSaved);

        return commandeFournisseur;
    }

    @Override
    public CommandeFournisseurDto deleteArticle(Integer idCommande, Integer idLigneCommande) {
        checkIdCommande(idCommande);
        checkIdLigneCommande(idLigneCommande);

        CommandeFournisseurDto commandeFournisseur = checkEtatCommande(idCommande);
        findLigneCommandeFournisseur(idLigneCommande);

        ligneCommandeFournisseurRepository.deleteById(idLigneCommande);

        return commandeFournisseur;
    }

    @Override
    public List<CommandeFournisseurDto> findAll() {
        return commandeFournisseurRepository.findAll().stream()
                .map(CommandeFournisseurDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<LigneCommandeFournisseurDto> findAllLignesCommandeFournisseurByCommandeFournisseurId(Integer idCommande) {
        return ligneCommandeFournisseurRepository.findAllByCommandeFournisseurId(idCommande)
                .stream().map(LigneCommandeFournisseurDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(Integer id) {
        if (id == null) {
            log.error("Commande fournisseur ID is null");
            return;
        }
        commandeFournisseurRepository.deleteById(id);
    }

    private void checkIdCommande(Integer idCommande) {
        if (idCommande == null) {
            log.error("Commande fournisseur ID is NULL");
            throw new InvalidOperationException(
                    "Impossible de modifier l'état de la commande fournisseur un ID null",
                    ErrorCodes.COMMANDE_FOURNISSEUR_NON_MODIFIABLE
            );
        }
    }

    private void checkIdLigneCommande(Integer idLigneCommande) {
        if (idLigneCommande == null) {
            log.error("L'ID de la ligne de commande fournisseur is NULL");
            throw new InvalidOperationException(
                    "Impossible de modifier l'état de la commande fournisseur avec ligne de commande null",
                    ErrorCodes.COMMANDE_FOURNISSEUR_NON_MODIFIABLE
            );
        }
    }

    private void checkIdArticle(Integer idArticle, String msg) {
        if (idArticle == null) {
            log.error("L'ID de l'article is NULL");
            throw new InvalidOperationException(
                    "Impossible de modifier l'état de la commande fournisseur avec un " + msg + " ID Article null",
                    ErrorCodes.COMMANDE_FOURNISSEUR_NON_MODIFIABLE
            );
        }
    }

    private CommandeFournisseurDto checkEtatCommande(Integer idCommande) {
        CommandeFournisseurDto commandeFournisseur = findById(idCommande);
        if (commandeFournisseur.isCommandeLivree()) {
            throw new InvalidOperationException(
                    "Impossible de modifier l'état de la commande fournisseur lorsqu'elle est livrée",
                    ErrorCodes.COMMANDE_FOURNISSEUR_NON_MODIFIABLE
            );
        }
        return commandeFournisseur;
    }


    private Optional<LigneCommandeFournisseur> findLigneCommandeFournisseur(Integer idLigneCommande) {
        Optional<LigneCommandeFournisseur> ligneCommandeFounisseurOptional = ligneCommandeFournisseurRepository.findById(idLigneCommande);
        if (ligneCommandeFounisseurOptional.isEmpty()) {
            throw new EntityNotFoundException(
                    "Impossible de trouver la ligne de commande fournisseur avec l'ID : " + idLigneCommande,
                    ErrorCodes.COMMANDE_FOURNISSEUR_NOT_FOUND
            );
        }
        return ligneCommandeFounisseurOptional;
    }

    private void updateMvtStk(Integer idCommande) {
        List<LigneCommandeFournisseur> ligneCommandeFournisseurs= ligneCommandeFournisseurRepository.findAllByCommandeFournisseurId(idCommande);
        ligneCommandeFournisseurs.forEach(ligne -> {
            MvtStkDto mvtStkDto = MvtStkDto.builder()
                    .article(ArticleDto.fromEntity(ligne.getArticle()))
                    .dateMvt(Instant.now())
                    .typeMvt(TypeMvtStk.ENTREE)
                    .sourceMvtStk(SourceMvtStk.COMMANDE_FOURNISSEUR)
                    .quantite(ligne.getQuantite())
                    .idEntreprise(ligne.getIdEntreprise())
                    .build();
            mvtStkService.sortieStock(mvtStkDto);
        });
    }
}
