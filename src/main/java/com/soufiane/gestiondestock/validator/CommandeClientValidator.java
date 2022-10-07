package com.soufiane.gestiondestock.validator;

import com.soufiane.gestiondestock.dto.CommandeClientDto;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class CommandeClientValidator {

    public static List<String> validate(CommandeClientDto commandeClientDto) {
        List<String> errors = new ArrayList<>();

        if (commandeClientDto == null) {
            errors.add("Veuillez renseigner le code de la commande client");
            errors.add("Veuillez renseigner la date de la commande client");
            errors.add("Veuillez selectionner un client de la commande client");
            errors.add("Veuillez selectionner les lignes commandes clients de la commande client");
            return errors;
        }

        if (!StringUtils.hasLength(commandeClientDto.getCode())) {
            errors.add("Veuillez renseigner le code de la commande client");
        }
        if (commandeClientDto.getDateCommande() == null) {
            errors.add("Veuillez renseigner la date de la commande client");
        }
        if (commandeClientDto.getEtatCommande() == null) {
            errors.add("Veuillez renseigner l'etat de la commande client");
        }
        if (commandeClientDto.getClient() == null || commandeClientDto.getClient().getId() == null) {
            errors.add("Veuillez selectionner un client de la commande client");
        }
        if (commandeClientDto.getLigneCommandeClients() == null) {
            errors.add("Veuillez selectionner les lignes commandes clients de la commande client");
        }

        return errors;
    }
}
