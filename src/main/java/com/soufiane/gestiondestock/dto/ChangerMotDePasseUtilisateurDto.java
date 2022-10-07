package com.soufiane.gestiondestock.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChangerMotDePasseUtilisateurDto {

    private Integer id;

    private String motDePasse;

    private String confirmMotDePasse;
}
