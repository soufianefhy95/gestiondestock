package com.soufiane.gestiondestock.dto;

import com.soufiane.gestiondestock.model.Roles;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RolesDto {

    private Integer id;

    private String roleName;

    private UtilisateurDto utilisateur;

    public static RolesDto fromEntity(Roles roles) {
        if (roles == null) {
            return null;
        }

        return RolesDto.builder()
                .id(roles.getId())
                .roleName(roles.getRoleName())
//                .utilisateur(UtilisateurDto.fromEntity(roles.getUtilisateur()))
                .build();
    }

//    public static List<RolesDto> fromListEntity(List<Roles> roles) {
//        if (roles == null) {
//            return null;
//        }
//        List<RolesDto> rolesDtos = new ArrayList<>();
//
//        roles.forEach(roleDto -> {
//            rolesDtos.add(RolesDto.builder()
//                    .id(roleDto.getId())
//                    .roleName(roleDto.getRoleName())
////                    .utilisateur(UtilisateurDto.fromEntity(roleDto.getUtilisateur()))
//                    .build());
//        });
//
//        return rolesDtos;
//    }

    public static Roles toEntity(RolesDto rolesDto) {
        if (rolesDto == null) {
            return null;
        }

        Roles roles = new Roles();
        roles.setId(rolesDto.getId());
        roles.setRoleName(rolesDto.getRoleName());
        roles.setUtilisateur(UtilisateurDto.toEntity(rolesDto.getUtilisateur()));
        return roles;
    }
}
