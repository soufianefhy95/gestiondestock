package com.soufiane.gestiondestock.validator;

import com.soufiane.gestiondestock.dto.MvtStkDto;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class MvtStkValidator {

    public static List<String> validate(MvtStkDto mvtStkDto) {
        List<String> errors = new ArrayList<>();

        if (mvtStkDto == null) {
            errors.add("Veuillez renseigner la date mouvement du mouvement de stock");
            errors.add("Veuillez renseigner la quantite du mouvement stock");
            errors.add("Veuillez renseigner l'article du mouvement stock");
            errors.add("Veuillez renseigner le type du mouvement stock");
            return errors;
        }

        if (mvtStkDto.getDateMvt() == null) {
            errors.add("Veuillez renseigner la date mouvement du mouvement de stock");
        }
        if (mvtStkDto.getQuantite() == null) {
            errors.add("Veuillez renseigner la quantite du mouvement stock");
        }
        if (mvtStkDto.getArticle() == null || mvtStkDto.getArticle().getId() == null) {
            errors.add("Veuillez renseigner l'article du mouvement stock");
        }
        if (!StringUtils.hasLength(mvtStkDto.getTypeMvt().name())) {
            errors.add("Veuillez renseigner le type du mouvement stock");
        }

        return errors;
    }
}
