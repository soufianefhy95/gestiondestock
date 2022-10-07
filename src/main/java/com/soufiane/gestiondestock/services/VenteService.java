package com.soufiane.gestiondestock.services;

import com.soufiane.gestiondestock.dto.VenteDto;

import java.util.List;

public interface VenteService {

    VenteDto save(VenteDto dto);

    VenteDto findById(Integer id);

    VenteDto findByCode(String code);

    List<VenteDto> findAll();

    void delete(Integer id);
}
