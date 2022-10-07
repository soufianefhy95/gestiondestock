package com.soufiane.gestiondestock.services.impl;

import com.soufiane.gestiondestock.dto.FournisseurDto;
import com.soufiane.gestiondestock.exception.EntityNotFoundException;
import com.soufiane.gestiondestock.exception.ErrorCodes;
import com.soufiane.gestiondestock.exception.InvalidEntityException;
import com.soufiane.gestiondestock.model.Fournisseur;
import com.soufiane.gestiondestock.repository.FournisseurRepository;
import com.soufiane.gestiondestock.services.FournisseurService;
import com.soufiane.gestiondestock.validator.FournisseurValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FournisseurServiceImpl implements FournisseurService {

    private FournisseurRepository fournisseurRepository;

    @Autowired
    public FournisseurServiceImpl(
            FournisseurRepository fournisseurRepository
    ) {
        this.fournisseurRepository = fournisseurRepository;
    }

    @Override
    public FournisseurDto save(FournisseurDto dto) {
        List<String> errors = FournisseurValidator.validate(dto);
        if (!errors.isEmpty()) {
            log.error("Fournisseur ID is not valid {}", dto);
            throw new InvalidEntityException("Les infos fournisseurs ne sont pas valides", ErrorCodes.FOURNISSEUR_NOT_FOUND, errors);
        }
        return FournisseurDto.fromEntity(
                fournisseurRepository.save(
                        FournisseurDto.toEntity(dto)
                )
        );
    }

    @Override
    public FournisseurDto findById(Integer id) {
        if (id == null) {
            log.error("Fournisseur ID is null");
            return null;
        }

        return fournisseurRepository.findById(id)
                .map(FournisseurDto::fromEntity)
                .orElseThrow(()-> new EntityNotFoundException(
                        "Aucun fournissur avec l'ID = "+ id +" n'a ete trouve dans la BDD",
                        ErrorCodes.FOURNISSEUR_NOT_FOUND)
        );
    }

    @Override
    public List<FournisseurDto> findAll() {
        return fournisseurRepository.findAll().stream()
                .map(FournisseurDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(Integer id) {
        if (id == null) {
            log.error("Fournisseur ID is null");
            return;
        }
        fournisseurRepository.deleteById(id);
    }
}
