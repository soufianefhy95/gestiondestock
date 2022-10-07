package com.soufiane.gestiondestock.services.impl;

import com.soufiane.gestiondestock.dto.CategoryDto;
import com.soufiane.gestiondestock.exception.EntityNotFoundException;
import com.soufiane.gestiondestock.exception.ErrorCodes;
import com.soufiane.gestiondestock.exception.InvalidEntityException;
import com.soufiane.gestiondestock.services.CategoryService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.swing.*;

import static org.junit.jupiter.api.Assertions.*;


@RunWith(SpringRunner.class)
@SpringBootTest
public class CategoryServiceImplTest {

    @Autowired
    private CategoryService service;

    @Test
    public void shouldSaveCategoryWithSuccess() {
        CategoryDto expectedCategory = CategoryDto.builder()
                .code("Test")
                .designation("designation")
                .idEntreprise(1)
                .build();

        CategoryDto savedCategory = service.save(expectedCategory);

        assertNotNull(savedCategory);
        assertNotNull(savedCategory.getId());
        assertEquals(expectedCategory.getCode(), savedCategory.getCode());
        assertEquals(expectedCategory.getDesignation(), savedCategory.getDesignation());
        assertEquals(expectedCategory.getIdEntreprise(), savedCategory.getIdEntreprise());
    }

    @Test
    public void shouldUpdateCategoryWithSuccess() {
        CategoryDto expectedCategory = CategoryDto.builder()
                .code("Test")
                .designation("designation")
                .idEntreprise(1)
                .build();

        CategoryDto savedCategory = service.save(expectedCategory);

        CategoryDto categoryToUpdate = savedCategory;
        categoryToUpdate.setCode("Cat update");

        service.save(categoryToUpdate);

        assertNotNull(categoryToUpdate);
        assertNotNull(categoryToUpdate.getId());
        assertEquals(categoryToUpdate.getCode(), savedCategory.getCode());
        assertEquals(categoryToUpdate.getDesignation(), savedCategory.getDesignation());
        assertEquals(categoryToUpdate.getIdEntreprise(), savedCategory.getIdEntreprise());
    }

    @Test
    public void shouldThrowInvalidEntityException() {
        CategoryDto expectedCategory = CategoryDto.builder()
                .build();

        InvalidEntityException excpectedException = assertThrows(InvalidEntityException.class, () -> service.save(expectedCategory));

        assertEquals(ErrorCodes.CATEGORY_NOT_VALID, excpectedException.getErrorCode());
        assertEquals(1, excpectedException.getErrors().size());
        assertEquals("Veuillez renseigner le code de la categorie", excpectedException.getErrors().get(0));
    }

    @Test
    public void shouldThrowEntityNotFoundException() {
        EntityNotFoundException excpectedException = assertThrows(EntityNotFoundException.class, () -> service.findById(0));

        assertEquals(ErrorCodes.CATEGORY_NOT_FOUND, excpectedException.getErrorCode());
        assertEquals("Aucune categorie avec l'ID = 0 n'a ete trouve dans la BDD", excpectedException.getMessage());
    }

    @Test(expected = EntityNotFoundException.class)
    public void shouldThrowEntityNotFoundException2() {
        service.findById(0);
    }
}