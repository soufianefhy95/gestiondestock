package com.soufiane.gestiondestock.validator;

import com.soufiane.gestiondestock.dto.CommandeFournisseurDto;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class CommandeFournisseurValidator {

    public static List<String> validate(CommandeFournisseurDto commandeFournisseurDto) {
        List<String> errors = new ArrayList<>();

        if (commandeFournisseurDto == null) {
            errors.add("Veuillez renseigner le code de la commande fournisseur");
            errors.add("Veuillez renseigner la date de la commande fournisseur");
            errors.add("Veuillez selectionner un fournisseur de la commande fournisseur");
            errors.add("Veuillez selectionner les lignes commandes fournisseurs de la commande fournisseur");
            return errors;
        }

        if (!StringUtils.hasLength(commandeFournisseurDto.getCode())) {
            errors.add("Veuillez renseigner le code de la commande fournisseur");
        }
        if (commandeFournisseurDto.getDateCommande() == null) {
            errors.add("Veuillez renseigner la date de la commande fournisseur");
        }
        if (commandeFournisseurDto.getFournisseur() == null || commandeFournisseurDto.getFournisseur().getId() == null) {
            errors.add("Veuillez selectionner un fournisseur de la commande fournisseur");
        }
        if (commandeFournisseurDto.getLigneCommandeFournisseurs() == null) {
            errors.add("Veuillez selectionner les lignes commandes fournisseurs de la commande fournisseur");
        }

        return errors;
    }
}
