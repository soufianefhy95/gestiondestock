package com.soufiane.gestiondestock.repository;

import com.soufiane.gestiondestock.model.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UtilisateurRepository extends JpaRepository<Utilisateur, Integer> {

//    JPQL queries
    @Query("select u from Utilisateur u where u.email = :email")
    Optional<Utilisateur> findUtilisateurByEmail(String email);
}
