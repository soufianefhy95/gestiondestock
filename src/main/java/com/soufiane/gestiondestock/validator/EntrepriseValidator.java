package com.soufiane.gestiondestock.validator;

import com.soufiane.gestiondestock.dto.EntrepriseDto;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class EntrepriseValidator {

    public static List<String> validate(EntrepriseDto entrepriseDto) {
        List<String> errors = new ArrayList<>();

        if (entrepriseDto == null) {
            errors.add("Veuillez renseigner le nom de l'entreprise");
            errors.add("Veuillez renseigner la description de l'entreprise");
            errors.add("Veuillez renseigner le code fiscal de l'entreprise");
            errors.add("Veuillez renseigner la photo de l'entreprise");
            errors.add("Veuillez renseigner l'email de l'entreprise");
            errors.addAll(AdresseValidator.validate(null));
            errors.add("Veuillez renseigner le numero de telephone de l'entreprise");
            errors.add("Veuillez renseigner le site web de l'entreprise");
            errors.add("Veuillez renseigner les utilisateurs de l'entreprise");
            return errors;
        }

        if (!StringUtils.hasLength(entrepriseDto.getNom())) {
            errors.add("Veuillez renseigner le nom de l'entreprise");
        }
        if (!StringUtils.hasLength(entrepriseDto.getDescription())) {
            errors.add("Veuillez renseigner la description de l'entreprise");
        }
        if (!StringUtils.hasLength(entrepriseDto.getCodeFiscal())) {
            errors.add("Veuillez renseigner le code fiscal de l'entreprise");
        }
        if (!StringUtils.hasLength(entrepriseDto.getPhoto())) {
            errors.add("Veuillez renseigner la photo de l'entreprise");
        }
        if (!StringUtils.hasLength(entrepriseDto.getEmail())) {
            errors.add("Veuillez renseigner l'email de l'entreprise");
        }

        errors.addAll(AdresseValidator.validate(entrepriseDto.getAdresse()));

        if (!StringUtils.hasLength(entrepriseDto.getNumTel())) {
            errors.add("Veuillez renseigner le numero de telephone de l'entreprise");
        }
        if (!StringUtils.hasLength(entrepriseDto.getSiteWeb())) {
            errors.add("Veuillez renseigner le site web de l'entreprise");
        }
        if (entrepriseDto.getUtilisateurs() == null) {
            errors.add("Veuillez renseigner les utilisateurs de l'entreprise");
        }

        return errors;
    }
}
