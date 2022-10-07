package com.soufiane.gestiondestock.services;

import com.soufiane.gestiondestock.dto.ChangerMotDePasseUtilisateurDto;
import com.soufiane.gestiondestock.dto.UtilisateurDto;

import java.util.List;

public interface UtilisateurService {

    UtilisateurDto save(UtilisateurDto dto);

    UtilisateurDto findById(Integer id);

    UtilisateurDto findByEmail(String email);

    UtilisateurDto changerMotDePasse(ChangerMotDePasseUtilisateurDto dto);

    List<UtilisateurDto> findAll();

    void delete(Integer id);
}
