package com.esgis2026.assigame.service;

import com.esgis2026.assigame.config.JwtUtils;
import com.esgis2026.assigame.dto.AuthResponse;
import com.esgis2026.assigame.dto.LoginRequest;
import com.esgis2026.assigame.dto.UpdateProfileRequest;
import com.esgis2026.assigame.dto.UtilisateurDto;
import com.esgis2026.assigame.entity.Utilisateur;
import com.esgis2026.assigame.repository.UtilisateurRepository;
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
    private final UtilisateurRepository utilisateurRepository;
    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;

    public AuthResponse register(Utilisateur request) {
        Utilisateur saved = utilisateurService.createUtilisateur(request);
        // On recharge l'utilisateur pour disposer du type_utilisateur complet (libellé du rôle).
        Utilisateur user = utilisateurRepository.findById(saved.getId_utilisateur()).orElse(saved);

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
                .user(UtilisateurDto.fromEntity(user))
                .build();
    }

    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getLogin(),
                        request.getPassword()
                )
        );
        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getLogin());
        String jwtToken = jwtUtils.generateToken(userDetails);

        Utilisateur user = utilisateurRepository.findByLogin_utilisateur(request.getLogin())
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));

        return AuthResponse.builder()
                .token(jwtToken)
                .user(UtilisateurDto.fromEntity(user))
                .build();
    }

    public UtilisateurDto getCurrentUser(String login) {
        Utilisateur user = utilisateurRepository.findByLogin_utilisateur(login)
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));
        return UtilisateurDto.fromEntity(user);
    }

    public UtilisateurDto updateCurrentUser(String login, UpdateProfileRequest request) {
        Utilisateur updated = utilisateurService.updateProfile(login, request);
        return UtilisateurDto.fromEntity(updated);
    }
}
