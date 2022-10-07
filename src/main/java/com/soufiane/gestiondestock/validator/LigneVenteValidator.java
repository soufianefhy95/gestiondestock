package com.soufiane.gestiondestock.validator;

import com.soufiane.gestiondestock.dto.LigneVenteDto;

import java.util.ArrayList;
import java.util.List;

public class LigneVenteValidator {

    public static List<String> validate(LigneVenteDto ligneVenteDto) {
        List<String> errors = new ArrayList<>();

        if (ligneVenteDto == null) {
            errors.add("Veuillez renseigner la vente de la ligne vente");
            errors.add("Veuillez renseigner la quantite de la ligne vente");
            errors.add("Veuillez renseigner le prix unitaire de la ligne vente");
            return errors;
        }

        if (ligneVenteDto.getVente() == null) {
            errors.add("Veuillez renseigner la vente de la ligne vente");
        }

        if (ligneVenteDto.getQuantite() == null) {
            errors.add("Veuillez renseigner la quantite de la ligne vente");
        }

        if (ligneVenteDto.getPrixUnitaire() == null) {
            errors.add("Veuillez renseigner le prix unitaire de la ligne vente");
        }

        return errors;
    }

}
