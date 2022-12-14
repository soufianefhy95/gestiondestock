package com.soufiane.gestiondestock.services.impl;

import com.soufiane.gestiondestock.dto.*;
import com.soufiane.gestiondestock.exception.EntityNotFoundException;
import com.soufiane.gestiondestock.exception.ErrorCodes;
import com.soufiane.gestiondestock.exception.InvalidEntityException;
import com.soufiane.gestiondestock.exception.InvalidOperationException;
import com.soufiane.gestiondestock.model.*;
import com.soufiane.gestiondestock.repository.ArticleRepository;
import com.soufiane.gestiondestock.repository.ClientRepository;
import com.soufiane.gestiondestock.repository.CommandeClientRepository;
import com.soufiane.gestiondestock.repository.LigneCommandeClientRepository;
import com.soufiane.gestiondestock.services.CommandeClientService;
import com.soufiane.gestiondestock.services.MvtStkService;
import com.soufiane.gestiondestock.validator.ArticleValidator;
import com.soufiane.gestiondestock.validator.CommandeClientValidator;
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
public class CommandeClientServiceImpl implements CommandeClientService {

    private CommandeClientRepository commandeClientRepository;
    private LigneCommandeClientRepository ligneCommandeClientRepository;
    private ClientRepository clientRepository;
    private ArticleRepository articleRepository;

    private MvtStkService mvtStkService;

    public CommandeClientServiceImpl(CommandeClientRepository commandeClientRepository, ClientRepository clientRepository, ArticleRepository articleRepository, LigneCommandeClientRepository ligneCommandeClientRepository, MvtStkService mvtStkService) {
        this.commandeClientRepository = commandeClientRepository;
        this.clientRepository = clientRepository;
        this.articleRepository = articleRepository;
        this.ligneCommandeClientRepository = ligneCommandeClientRepository;
        this.mvtStkService = mvtStkService;
    }

    @Override
    public CommandeClientDto save(CommandeClientDto dto) {

        List<String> errors = CommandeClientValidator.validate(dto);

        if (!errors.isEmpty()) {
            log.error("Commande client n'est pas valide");
            throw new InvalidEntityException(
                    "La commande client n'est pas valide",
                    ErrorCodes.COMMANDE_CLIENT_NOT_VALID,
                    errors
            );
        }

        if (dto.getId() != null && dto.isCommandeLivree()) {
            throw new InvalidOperationException(
                    "Impossible de modifier l'ID : " + dto.getId() + " de cette commande client",
                    ErrorCodes.COMMANDE_CLIENT_NON_MODIFIABLE);
        }

        Optional<Client> client = clientRepository.findById(dto.getClient().getId());
        if (client.isEmpty()) {
            log.warn("Client with ID {} was not found in DB", dto.getClient().getId());
            throw new EntityNotFoundException(
                    "Aucun client avec l'ID : " + dto.getClient().getId() + " n'a ete trouve dans la DB",
                    ErrorCodes.CLIENT_NOT_FOUND
            );
        }

        List<String> articleErrors = new ArrayList<>();

        if (dto.getLigneCommandeClients() != null) {
            dto.getLigneCommandeClients().forEach(ligneCdeClient -> {
                if (ligneCdeClient.getArticle() != null) {
                    Optional<Article> article = articleRepository.findById(ligneCdeClient.getArticle().getId());
                    if (article.isEmpty()) {
                        articleErrors.add("L'article avec l'ID : "+ ligneCdeClient.getArticle().getId() +" n'existe pas");
                    }
                } else {
                        articleErrors.add("Impossible d'enregistrer une commande avec un article NULL");
                }
            });
        }

        if (!articleErrors.isEmpty()) {
            log.warn("La commande client ne peut pas etre enregistree");
            throw new InvalidEntityException("Un ou plusieurs articles que vous avez fourni n'existent pas", ErrorCodes.ARTICLE_NOT_FOUND, articleErrors);
        }

        // Saving 'commande client'
        CommandeClient savedCmdClt = commandeClientRepository.save(CommandeClientDto.toEntity(dto));

        if (dto.getLigneCommandeClients() != null) {
            // Saving 'ligne commande clients'
            dto.getLigneCommandeClients().forEach(ligneCmdClt -> {
                LigneCommandeClient ligneCommandeClient = LigneCommandeClientDto.toEntity(ligneCmdClt);
                ligneCommandeClient.setCommandeClient(savedCmdClt);
                ligneCommandeClientRepository.save(ligneCommandeClient);
            });
        }
        return CommandeClientDto.fromEntity(savedCmdClt);
    }

    @Override
    public CommandeClientDto findById(Integer id) {
        if (id == null) {
            log.error("Commande client ID is NULL");
            return null;
        }
        return commandeClientRepository.findById(id)
                .map(CommandeClientDto::fromEntity)
                .orElseThrow(()-> new EntityNotFoundException(
                        "Aucune commande client n'a ete trouvee avec le 'ID : " + id,
                        ErrorCodes.COMMANDE_CLIENT_NOT_FOUND
                ));
    }

    @Override
    public CommandeClientDto findByCode(String code) {
        if (!StringUtils.hasLength(code)) {
            log.error("Commande client ID is NULL");
            return null;
        }
        return commandeClientRepository.findCommandeClientByCode(code)
                .map(CommandeClientDto::fromEntity)
                .orElseThrow(()-> new EntityNotFoundException(
                        "Aucune commande client n'a ete trouvee avec le code : " + code,
                        ErrorCodes.COMMANDE_CLIENT_NOT_FOUND
                ));
    }

    @Override
    public List<CommandeClientDto> findAll() {
        return commandeClientRepository.findAll().stream()
                .map(CommandeClientDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<LigneCommandeClientDto> findAllLignesCommandeClientByCommandeClientId(Integer idCommande) {
        return ligneCommandeClientRepository.findAllByCommandeClientId(idCommande)
                .stream().map(LigneCommandeClientDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(Integer id) {
        if (id == null) {
            log.error("Commande client ID is null");
            return;
        }
        commandeClientRepository.deleteById(id);
    }

    @Override
    public CommandeClientDto updateEtatCommande(Integer idCommande, EtatCommande etatCommande) {
        checkIdCommande(idCommande);

        if (!StringUtils.hasLength(String.valueOf(etatCommande))) {
            log.error("L'??tat de la commande client is NULL");
            throw new InvalidOperationException(
                    "Impossible de modifier l'??tat de la commande avec un etat null",
                    ErrorCodes.COMMANDE_CLIENT_NON_MODIFIABLE
            );
        }

        CommandeClientDto commandeClient = checkEtatCommande(idCommande);
        commandeClient.setEtatCommande(etatCommande);

        CommandeClient savedCmdClt = commandeClientRepository.save(CommandeClientDto.toEntity(commandeClient));
        if (commandeClient.isCommandeLivree()) {
            updateMvtStk(idCommande);
        }
        return CommandeClientDto.fromEntity(savedCmdClt);
    }

    @Override
    public CommandeClientDto updateQuantiteCommande(Integer idCommande, Integer idLigneCommande, BigDecimal quantite) {
        checkIdCommande(idCommande);
        checkIdLigneCommande(idLigneCommande);

        if (quantite == null || quantite.compareTo(BigDecimal.ZERO) == 0) {
            log.error("L'ID de la ligne de commande client is NULL");
            throw new InvalidOperationException(
                    "Impossible de modifier l'??tat de la commande avec une quantite null ou ZERO",
                    ErrorCodes.COMMANDE_CLIENT_NON_MODIFIABLE
            );
        }


        CommandeClientDto commandeClient = checkEtatCommande(idCommande);

        Optional<LigneCommandeClient> ligneCommandeClientOptional = findLigneCommandeClient(idLigneCommande);

        LigneCommandeClient ligneCommandeClient = ligneCommandeClientOptional.get();
        ligneCommandeClient.setQuantite(quantite);
        ligneCommandeClientRepository.save(ligneCommandeClient);

        return commandeClient;
    }

    @Override
    public CommandeClientDto updateClient(Integer idCommande, Integer idClient) {
        checkIdCommande(idCommande);
        if (idClient == null) {
            log.error("L'ID du client de la commande client is NULL");
            throw new InvalidOperationException(
                    "Impossible de modifier l'??tat de la commande avec un ID de client null",
                    ErrorCodes.COMMANDE_CLIENT_NON_MODIFIABLE
            );
        }

        CommandeClientDto commandeClient = checkEtatCommande(idCommande);

        Optional<Client> clientOptional = clientRepository.findById(idClient);
        if (clientOptional.isEmpty()) {
            throw new EntityNotFoundException(
                    "Aucun client n'a ??t?? trouv?? avec l'ID " + idClient,
                    ErrorCodes.CLIENT_NOT_FOUND
            );
        }

        commandeClient.setClient(ClientDto.fromEntity(clientOptional.get()));

        return CommandeClientDto.fromEntity(
                commandeClientRepository.save(
                        CommandeClientDto.toEntity(commandeClient)
                )
        );
    }

    @Override
    public CommandeClientDto updateArticle(Integer idCommande, Integer idLigneCommande, Integer newIdArticle) {
        checkIdCommande(idCommande);
        checkIdLigneCommande(idLigneCommande);
        checkIdArticle(newIdArticle, "nouvel");

        CommandeClientDto commandeClient = checkEtatCommande(idCommande);
        Optional<LigneCommandeClient> ligneCommandeClient = findLigneCommandeClient(idLigneCommande);

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

        LigneCommandeClient ligneCommandeClientToSaved = ligneCommandeClient.get();
        ligneCommandeClientToSaved.setArticle(articleOptional.get());
        ligneCommandeClientRepository.save(ligneCommandeClientToSaved);

        return commandeClient;
    }

    @Override
    public CommandeClientDto deleteArticle(Integer idCommande, Integer idLigneCommande) {
        checkIdCommande(idCommande);
        checkIdLigneCommande(idLigneCommande);

        CommandeClientDto commandeClient = checkEtatCommande(idCommande);
        findLigneCommandeClient(idLigneCommande);

        ligneCommandeClientRepository.deleteById(idLigneCommande);

        return commandeClient;
    }

    private void checkIdCommande(Integer idCommande) {
        if (idCommande == null) {
            log.error("Commande client ID is NULL");
            throw new InvalidOperationException(
                    "Impossible de modifier l'??tat de la commande avec un ID null",
                    ErrorCodes.COMMANDE_CLIENT_NON_MODIFIABLE
            );
        }
    }

    private void checkIdLigneCommande(Integer idLigneCommande) {
        if (idLigneCommande == null) {
            log.error("L'ID de la ligne de commande client is NULL");
            throw new InvalidOperationException(
                    "Impossible de modifier l'??tat de la commande avec ligne de commande null",
                    ErrorCodes.COMMANDE_CLIENT_NON_MODIFIABLE
            );
        }
    }

    private void checkIdArticle(Integer idArticle, String msg) {
        if (idArticle == null) {
            log.error("L'ID de l'article is NULL");
            throw new InvalidOperationException(
                    "Impossible de modifier l'??tat de la commande avec un " + msg + " ID Article null",
                    ErrorCodes.COMMANDE_CLIENT_NON_MODIFIABLE
            );
        }
    }

    private CommandeClientDto checkEtatCommande(Integer idCommande) {
        CommandeClientDto commandeClient = findById(idCommande);
        if (commandeClient.isCommandeLivree()) {
            throw new InvalidOperationException(
                    "Impossible de modifier l'??tat de la commande lorsqu'elle est livr??e",
                    ErrorCodes.COMMANDE_CLIENT_NON_MODIFIABLE
            );
        }
        return commandeClient;
    }


    private Optional<LigneCommandeClient> findLigneCommandeClient(Integer idLigneCommande) {
        Optional<LigneCommandeClient> ligneCommandeClientOptional = ligneCommandeClientRepository.findById(idLigneCommande);
        if (ligneCommandeClientOptional.isEmpty()) {
            throw new EntityNotFoundException(
                    "Impossible de trouver la ligne de commande client avec l'ID : " + idLigneCommande,
                    ErrorCodes.COMMANDE_CLIENT_NOT_FOUND
            );
        }
        return ligneCommandeClientOptional;
    }

    private void updateMvtStk(Integer idCommande) {
        List<LigneCommandeClient> ligneCommandeClients = ligneCommandeClientRepository.findAllByCommandeClientId(idCommande);
        ligneCommandeClients.forEach(ligne -> {
            MvtStkDto mvtStkDto = MvtStkDto.builder()
                    .article(ArticleDto.fromEntity(ligne.getArticle()))
                    .dateMvt(Instant.now())
                    .typeMvt(TypeMvtStk.SORTIE)
                    .sourceMvtStk(SourceMvtStk.COMMANDE_CLIENT)
                    .quantite(ligne.getQuantite())
                    .idEntreprise(ligne.getIdEntreprise())
                    .build();
            mvtStkService.sortieStock(mvtStkDto);
        });
    }
}
