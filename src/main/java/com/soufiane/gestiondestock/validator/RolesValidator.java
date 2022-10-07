package com.soufiane.gestiondestock.validator;

import com.soufiane.gestiondestock.dto.RolesDto;

import java.util.ArrayList;
import java.util.List;

public class RolesValidator {

    public static List<String> validate(RolesDto rolesDto) {
        List<String> errors = new ArrayList<>();

        if (rolesDto == null) {
            errors.add("Veuillez renseigner le role name");
            errors.add("Veuillez renseigner l'utilisateur des roles");
            return errors;
        }

        if (rolesDto.getRoleName() == null) {
            errors.add("Veuillez renseigner le role name");
        }
        if (rolesDto.getUtilisateur() == null) {
            errors.add("Veuillez renseigner l'utilisateur des roles");
        }

        return errors;
    }
}
