package com.soufiane.gestiondestock.services.impl;

import com.soufiane.gestiondestock.dto.CategoryDto;
import com.soufiane.gestiondestock.exception.EntityNotFoundException;
import com.soufiane.gestiondestock.exception.ErrorCodes;
import com.soufiane.gestiondestock.exception.InvalidEntityException;
import com.soufiane.gestiondestock.model.Category;
import com.soufiane.gestiondestock.repository.CategoryRepository;
import com.soufiane.gestiondestock.services.CategoryService;
import com.soufiane.gestiondestock.validator.CategoryValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CategoryServiceImpl implements CategoryService {

    private CategoryRepository categoryRepository;

    @Autowired
    public CategoryServiceImpl(
            CategoryRepository categoryRepository
    ) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public CategoryDto save(CategoryDto dto) {
        List<String> errors = CategoryValidator.validate(dto);
        if (!errors.isEmpty()) {
            log.error("Category is not valid {}", dto);
            throw new InvalidEntityException("La category n'est pas valide", ErrorCodes.CATEGORY_NOT_VALID, errors);
        }
        return CategoryDto.fromEntity(
                categoryRepository.save(
                        CategoryDto.toEntity(dto)
                )
        );
    }

    @Override
    public CategoryDto findById(Integer id) {
        if (id == null) {
            log.error("Category ID is null");
            return null;
        }

        return categoryRepository.findById(id)
                .map(CategoryDto::fromEntity)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Aucune categorie avec l'ID = "+ id +" n'a ete trouve dans la BDD",
                        ErrorCodes.CATEGORY_NOT_FOUND)
        );
    }

    @Override
    public CategoryDto findByCode(String code) {
        if (!StringUtils.hasLength(code)) {
            log.error("Category code is null");
            return null;
        }

        return categoryRepository.findCategoryByCode(code)
                .map(CategoryDto::fromEntity)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Aucune categorie avec l'ID = "+ code +" n'a ete trouve dans la BDD",
                        ErrorCodes.CATEGORY_NOT_FOUND)
                );
    }

    @Override
    public List<CategoryDto> findAll() {
        return categoryRepository.findAll().stream()
                .map(CategoryDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(Integer id) {
        if (id == null) {
            log.error("Category ID is null");
            return;
        }
        categoryRepository.deleteById(id);
    }
}
