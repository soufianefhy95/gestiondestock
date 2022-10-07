package com.soufiane.gestiondestock.services;

import com.soufiane.gestiondestock.dto.MvtStkDto;

import java.math.BigDecimal;
import java.util.List;

public interface MvtStkService {

    BigDecimal stockReelArticle(Integer idArticle);

    List<MvtStkDto> mvtStkArticle(Integer idArticle);

    MvtStkDto entreeStock(MvtStkDto mvtStkDto);

    MvtStkDto sortieStock(MvtStkDto mvtStkDto);

    MvtStkDto correctionStockPos(MvtStkDto mvtStkDto);

    MvtStkDto correctionStockNeg(MvtStkDto mvtStkDto);

//    MvtStkDto save(MvtStkDto dto);
//
//    MvtStkDto findById(Integer id);
//
//    List<MvtStkDto> findAll();
//
//    void delete(Integer id);
}
