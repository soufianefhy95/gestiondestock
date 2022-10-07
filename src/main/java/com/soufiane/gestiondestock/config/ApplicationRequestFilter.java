package com.soufiane.gestiondestock.config;

import com.soufiane.gestiondestock.services.auth.ApplicationUserDetailsService;
import com.soufiane.gestiondestock.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class ApplicationRequestFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private ApplicationUserDetailsService applicationUserDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");

        String userEmail = null;
        String jwt = null;
        String idEntreprise = null;

        if (StringUtils.hasLength(authHeader)) {
            jwt = authHeader.substring(7);
            userEmail = jwtUtil.extractUsername(jwt);
            idEntreprise = jwtUtil.extractIdEntreprise(jwt);
        }

//        System.out.println("--> jwt : " + jwt);
//        System.out.println("--> userEmail : " + userEmail);
//        System.out.println("--> idEntreprise : " + idEntreprise);

        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = applicationUserDetailsService.loadUserByUsername(userEmail);

            if (jwtUtil.validateToken(jwt, userDetails)) {
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new
                        UsernamePasswordAuthenticationToken(
                                userDetails, null, userDetails.getAuthorities());

                usernamePasswordAuthenticationToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );

                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
        }
        MDC.put("id_entreprise", idEntreprise);
        filterChain.doFilter(request, response);
    }
}
