package com.soufiane.gestiondestock.dto;

import com.soufiane.gestiondestock.model.LigneVente;
import com.soufiane.gestiondestock.model.Vente;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VenteDto {

    private Integer id;

    private String code;

    private Instant dateVente;

    private String commentaire;

    private Integer idEntreprise;

    private List<LigneVenteDto> ligneVente;

    public static VenteDto fromEntity(Vente vente) {
        if (vente == null) {
            return null;
        }

        return VenteDto.builder()
                .id(vente.getId())
                .code(vente.getCode())
                .dateVente(vente.getDateVente())
                .commentaire(vente.getCommentaire())
                .idEntreprise(vente.getIdEntreprise())
                .build();
    }

    public static Vente toEntity(VenteDto venteDto) {
        if (venteDto == null) {
            return null;
        }

        Vente vente = new Vente();
        vente.setId(venteDto.getId());
        vente.setCode(venteDto.getCode());
        vente.setDateVente(venteDto.getDateVente());
        vente.setCommentaire(venteDto.getCommentaire());
        vente.setIdEntreprise(venteDto.getIdEntreprise());
        return vente;
    }
}
