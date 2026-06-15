package com.esgis2026.assigame.service;

import com.esgis2026.assigame.config.JwtUtils;
import com.esgis2026.assigame.dto.AuthResponse;
import com.esgis2026.assigame.dto.LoginRequest;
import com.esgis2026.assigame.entity.Utilisateur;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UtilisateurService utilisateurService;
    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;

    public AuthResponse register(Utilisateur request) {
        Utilisateur user = utilisateurService.createUtilisateur(request);
        
        String role = "ROLE_USER";
        if (user.getType_utilisateur() != null && user.getType_utilisateur().getLibelle_type_utilisateur() != null) {
            role = "ROLE_" + user.getType_utilisateur().getLibelle_type_utilisateur().toUpperCase();
        }

        UserDetails userDetails = User.builder()
                .username(user.getLogin_utilisateur())
                .password(user.getPassword_utilisateur())
                .authorities(role)
                .build();

        String jwtToken = jwtUtils.generateToken(userDetails);
        return AuthResponse.builder()
                .token(jwtToken)
                .build();
    }

    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getLogin(),
                        request.getPassword()
                )
        );
        UserDetails user = userDetailsService.loadUserByUsername(request.getLogin());
        String jwtToken = jwtUtils.generateToken(user);
        return AuthResponse.builder()
                .token(jwtToken)
                .build();
    }
}
