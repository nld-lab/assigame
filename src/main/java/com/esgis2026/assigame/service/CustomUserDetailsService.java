package com.esgis2026.assigame.service;

import com.esgis2026.assigame.entity.Utilisateur;
import com.esgis2026.assigame.repository.UtilisateurRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UtilisateurRepository utilisateurRepository;

    public CustomUserDetailsService(UtilisateurRepository utilisateurRepository) {
        this.utilisateurRepository = utilisateurRepository;
    }

    @Override
    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        Utilisateur utilisateur = utilisateurRepository.findByLogin_utilisateur(login)
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé avec le login: " + login));

        String role = "ROLE_USER";
        if (utilisateur.getType_utilisateur() != null) {
            role = "ROLE_" + utilisateur.getType_utilisateur().getLibelle_type_utilisateur().toUpperCase();
        }

        return new User(
                utilisateur.getLogin_utilisateur(),
                utilisateur.getPassword_utilisateur(),
                Collections.singletonList(new SimpleGrantedAuthority(role))
        );
    }
}
