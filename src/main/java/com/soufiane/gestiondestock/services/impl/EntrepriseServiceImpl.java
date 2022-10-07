package com.soufiane.gestiondestock.services.impl;

import com.soufiane.gestiondestock.dto.EntrepriseDto;
import com.soufiane.gestiondestock.dto.RolesDto;
import com.soufiane.gestiondestock.dto.UtilisateurDto;
import com.soufiane.gestiondestock.exception.EntityNotFoundException;
import com.soufiane.gestiondestock.exception.ErrorCodes;
import com.soufiane.gestiondestock.exception.InvalidEntityException;
import com.soufiane.gestiondestock.model.Entreprise;
import com.soufiane.gestiondestock.model.Roles;
import com.soufiane.gestiondestock.repository.EntrepriseRepository;
import com.soufiane.gestiondestock.repository.RolesRepository;
import com.soufiane.gestiondestock.services.EntrepriseService;
import com.soufiane.gestiondestock.services.UtilisateurService;
import com.soufiane.gestiondestock.validator.EntrepriseValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class EntrepriseServiceImpl implements EntrepriseService {

    private EntrepriseRepository entrepriseRepository;
    private UtilisateurService utilisateurService;
    private RolesRepository rolesRepository;

    @Autowired
    public EntrepriseServiceImpl(
            EntrepriseRepository entrepriseRepository,
            UtilisateurService utilisateurService,
            RolesRepository rolesRepository
    ) {
        this.entrepriseRepository = entrepriseRepository;
        this.utilisateurService = utilisateurService;
        this.rolesRepository = rolesRepository;
    }

    @Override
    public EntrepriseDto save(EntrepriseDto dto) {
        List<String> errors = EntrepriseValidator.validate(dto);
        if (!errors.isEmpty()) {
            log.error("Entreprise is not valid {}", dto);
            throw new InvalidEntityException("L'entreprise n'est pas valide", ErrorCodes.ENTREPRISE_NOT_FOUND, errors);
        }

        EntrepriseDto savedEntreprise = EntrepriseDto.fromEntity(
                entrepriseRepository.save(EntrepriseDto.toEntity(dto))
        );

        UtilisateurDto utilisateurDto = fromEntreprise(savedEntreprise);

        UtilisateurDto savedUser = utilisateurService.save(utilisateurDto);

        RolesDto rolesDto = RolesDto.builder()
                .roleName("ADMIN")
                .utilisateur(savedUser)
                .build();

        rolesRepository.save(RolesDto.toEntity(rolesDto));

        return savedEntreprise;
    }

    public UtilisateurDto fromEntreprise(EntrepriseDto dto) {
        return UtilisateurDto.builder()
                .adresse(dto.getAdresse())
                .nom(dto.getNom())
                .prenom(dto.getCodeFiscal())
                .email(dto.getEmail())
                .motDePasse(generateRandomPassword())
                .entreprise(dto)
                .dateDeNaissance(Instant.now())
                .photo(dto.getPhoto())
                .build();
    }

    private String generateRandomPassword() {return "soufianefhy";}

    @Override
    public EntrepriseDto findById(Integer id) {
        if (id == null) {
            log.error("Entreprise ID is null");
            return null;
        }

        return entrepriseRepository.findById(id)
                .map(EntrepriseDto::fromEntity)
                .orElseThrow(()-> new EntityNotFoundException(
                        "Aucune entreprise avec l'ID = "+ id +" n'a ete trouve dans la BDD",
                        ErrorCodes.ENTREPRISE_NOT_FOUND)
        );
    }

    @Override
    public EntrepriseDto findByCodeFiscal(String codeFiscal) {
        if (!StringUtils.hasLength(codeFiscal)) {
            log.error("Entreprise code fiscal is null");
            return null;
        }

        return entrepriseRepository.findEntrepriseByCodeFiscal(codeFiscal)
                .map(EntrepriseDto::fromEntity)
                .orElseThrow(()-> new EntityNotFoundException(
                        "Aucune entreprise avec le code = "+ codeFiscal +" n'a ete trouve dans la BDD",
                        ErrorCodes.ENTREPRISE_NOT_FOUND)
        );
    }

    @Override
    public List<EntrepriseDto> findAll() {
        return entrepriseRepository.findAll().stream()
                .map(EntrepriseDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(Integer id) {
        if (id == null) {
            log.error("Entreprise ID is null");
            return;
        }
        entrepriseRepository.deleteById(id);
    }
}
