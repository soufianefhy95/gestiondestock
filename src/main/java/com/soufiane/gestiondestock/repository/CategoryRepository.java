package com.soufiane.gestiondestock.repository;

import com.soufiane.gestiondestock.model.Category;
import com.soufiane.gestiondestock.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Integer> {

    Optional<Category> findCategoryByCode(String code);
}
