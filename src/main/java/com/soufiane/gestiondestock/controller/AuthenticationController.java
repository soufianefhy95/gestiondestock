package com.soufiane.gestiondestock.controller;

import com.soufiane.gestiondestock.dto.auth.AuthenticationRequest;
import com.soufiane.gestiondestock.dto.auth.AuthenticationResponse;
import com.soufiane.gestiondestock.model.Utilisateur;
import com.soufiane.gestiondestock.model.auth.ExtendedUser;
import com.soufiane.gestiondestock.services.auth.ApplicationUserDetailsService;
import com.soufiane.gestiondestock.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.soufiane.gestiondestock.utils.Constants.AUTHENTICATION_ENDPOINT;

@RestController
@RequestMapping(AUTHENTICATION_ENDPOINT)
@RequiredArgsConstructor
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private ApplicationUserDetailsService applicationUserDetailsService;
    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/authenticate")
    public AuthenticationResponse authenticate(@RequestBody AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getLogin(),
                        request.getMotDePasse()
                )
        );
        final UserDetails userDetails = applicationUserDetailsService.loadUserByUsername(request.getLogin());

        final String jwt = jwtUtil.generateToken((ExtendedUser) userDetails);

        return AuthenticationResponse.builder().accessToken(jwt).build();
    }
}
