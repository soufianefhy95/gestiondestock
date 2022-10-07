package com.soufiane.gestiondestock.validator;

import com.soufiane.gestiondestock.dto.FournisseurDto;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class FournisseurValidator {

    public static List<String> validate(FournisseurDto fournisseurDto) {
        List<String> errors = new ArrayList<>();

        if (fournisseurDto == null) {
            errors.add("Veuillez renseigner le nom du fournisseur");
            errors.add("Veuillez renseigner le prenom du fournisseur");
            errors.add("Veuillez renseigner l'email du fournisseur");
            errors.add("Veuillez renseigner le numero de telephone du fournisseur");
            errors.addAll(AdresseValidator.validate(null));
            errors.add("Veuillez renseigner les commandes fournisseurs du fournisseur");
            return errors;
        }

        if (!StringUtils.hasLength(fournisseurDto.getNom())) {
            errors.add("Veuillez renseigner le nom du fournisseur");
        }
        if (!StringUtils.hasLength(fournisseurDto.getPrenom())) {
            errors.add("Veuillez renseigner le prenom du fournisseur");
        }
        if (!StringUtils.hasLength(fournisseurDto.getMail())) {
            errors.add("Veuillez renseigner l'email du fournisseur");
        }
        if (!StringUtils.hasLength(fournisseurDto.getNumTel())) {
            errors.add("Veuillez renseigner le numero de telephone du fournisseur");
        }

        errors.addAll(AdresseValidator.validate(fournisseurDto.getAdresse()));

        if (fournisseurDto.getCommandeFournisseurs() == null) {
            errors.add("Veuillez renseigner les commandes fournisseurs du fournisseur");
        }

        return errors;
    }
}
