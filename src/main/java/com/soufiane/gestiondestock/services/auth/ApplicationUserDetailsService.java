package com.soufiane.gestiondestock.services.auth;

import com.soufiane.gestiondestock.dto.UtilisateurDto;
import com.soufiane.gestiondestock.exception.EntityNotFoundException;
import com.soufiane.gestiondestock.exception.ErrorCodes;
import com.soufiane.gestiondestock.model.Utilisateur;
import com.soufiane.gestiondestock.model.auth.ExtendedUser;
import com.soufiane.gestiondestock.repository.UtilisateurRepository;
import com.soufiane.gestiondestock.services.UtilisateurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class ApplicationUserDetailsService implements UserDetailsService {

    @Autowired
    private UtilisateurService utilisateurService;


    @Override
    public ExtendedUser loadUserByUsername(String email) throws UsernameNotFoundException {

        UtilisateurDto utilisateur = utilisateurService.findByEmail(email);

        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        utilisateur.getRoles().forEach(role -> authorities.add(new SimpleGrantedAuthority(role.getRoleName())));

        return new ExtendedUser(utilisateur.getEmail(), utilisateur.getMotDePasse(), utilisateur.getEntreprise().getId(), Collections.emptyList());
    }
}
