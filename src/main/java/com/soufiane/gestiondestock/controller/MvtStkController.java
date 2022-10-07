package com.soufiane.gestiondestock.controller;

import com.soufiane.gestiondestock.controller.api.MvtStkApi;
import com.soufiane.gestiondestock.dto.MvtStkDto;
import com.soufiane.gestiondestock.services.MvtStkService;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.List;

public class MvtStkController implements MvtStkApi {

    private MvtStkService mvtStkService;

    @Autowired
    public MvtStkController(MvtStkService mvtStkService) {
        this.mvtStkService = mvtStkService;
    }

    @Override
    public BigDecimal stockReelArticle(Integer idArticle) {
        return mvtStkService.stockReelArticle(idArticle);
    }

    @Override
    public List<MvtStkDto> mvtStkArticle(Integer idArticle) {
        return mvtStkService.mvtStkArticle(idArticle);
    }

    @Override
    public MvtStkDto entreeStock(MvtStkDto mvtStkDto) {
        return mvtStkService.entreeStock(mvtStkDto);
    }

    @Override
    public MvtStkDto sortieStock(MvtStkDto mvtStkDto) {
        return mvtStkService.sortieStock(mvtStkDto);
    }

    @Override
    public MvtStkDto correctionStockPos(MvtStkDto mvtStkDto) {
        return mvtStkService.correctionStockPos(mvtStkDto);
    }

    @Override
    public MvtStkDto correctionStockNeg(MvtStkDto mvtStkDto) {
        return mvtStkService.correctionStockNeg(mvtStkDto);
    }
}
