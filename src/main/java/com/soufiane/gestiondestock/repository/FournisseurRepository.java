package com.soufiane.gestiondestock.repository;

import com.soufiane.gestiondestock.model.Fournisseur;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FournisseurRepository extends JpaRepository<Fournisseur, Integer>  {
}
