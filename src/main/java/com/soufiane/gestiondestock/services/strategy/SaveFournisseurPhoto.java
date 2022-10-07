package com.soufiane.gestiondestock.services.strategy;

import com.flickr4java.flickr.FlickrException;
import com.soufiane.gestiondestock.dto.EntrepriseDto;
import com.soufiane.gestiondestock.dto.FournisseurDto;
import com.soufiane.gestiondestock.exception.ErrorCodes;
import com.soufiane.gestiondestock.exception.InvalidOperationException;
import com.soufiane.gestiondestock.model.Fournisseur;
import com.soufiane.gestiondestock.services.FlickrService;
import com.soufiane.gestiondestock.services.FournisseurService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.InputStream;

@Service("fournisseurStrategy")
@Slf4j
public class SaveFournisseurPhoto implements Strategy<FournisseurDto> {

    private FlickrService flickrService;
    private FournisseurService fournisseurService;

    @Autowired
    public SaveFournisseurPhoto(FlickrService flickrService, FournisseurService fournisseurService) {
        this.flickrService = flickrService;
        this.fournisseurService = fournisseurService;
    }

    @Override
    public FournisseurDto savePhoto(Integer id, InputStream photo, String titre) throws FlickrException {
        FournisseurDto fournisseur = fournisseurService.findById(id);
        String urlPhoto = flickrService.savePhoto(photo, titre);
        if (!StringUtils.hasLength(urlPhoto)) {
            throw new InvalidOperationException("Erreur lors de l'enregistrement de la photo du fournisseur", ErrorCodes.UPDATE_PHOTO_EXCEPTION);
        }
        fournisseur.setPhoto(urlPhoto);
        return fournisseurService.save(fournisseur);
    }
}
