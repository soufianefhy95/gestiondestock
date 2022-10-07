package com.soufiane.gestiondestock.repository;

import com.soufiane.gestiondestock.model.Entreprise;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EntrepriseRepository extends JpaRepository<Entreprise, Integer> {

    Optional<Entreprise> findEntrepriseByCodeFiscal(String code);
}
