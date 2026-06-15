package com.esgis2026.assigame.service;

import com.esgis2026.assigame.entity.Utilisateur;
import com.esgis2026.assigame.repository.UtilisateurRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UtilisateurService {
    final UtilisateurRepository utilisateurRepository;
    final PasswordEncoder passwordEncoder;

    public UtilisateurService(UtilisateurRepository utilisateurRepository, PasswordEncoder passwordEncoder) {
        this.utilisateurRepository = utilisateurRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<Utilisateur> getAllUtilisateurs() {
        return utilisateurRepository.findAll();
    }

    public Utilisateur createUtilisateur(Utilisateur utilisateur) {
        utilisateur.setPassword_utilisateur(passwordEncoder.encode(utilisateur.getPassword_utilisateur()));
        return utilisateurRepository.save(utilisateur);
    }

    public void deleteUtilisateur(Long idUtilisateur) {
        utilisateurRepository.deleteById(idUtilisateur);
    }

    public Utilisateur updateUtilisateur(Utilisateur details, Long idUtilisateur) {
        Utilisateur utiliateur = utilisateurRepository.findById(idUtilisateur)
                .orElseThrow(()->new RuntimeException("utilisateur n'existe pas"));
        utiliateur.setNom_utilisateur(details.getNom_utilisateur());
        utiliateur.setMail_utilisateur(details.getMail_utilisateur());
        utiliateur.setSexe_utilisateur(details.getSexe_utilisateur());
        utiliateur.setResidence_utilisateur(details.getResidence_utilisateur());
        utiliateur.setPrenom_utilisateur(details.getPrenom_utilisateur());
        utiliateur.setLogin_utilisateur(details.getLogin_utilisateur());
        
        if (details.getPassword_utilisateur() != null && !details.getPassword_utilisateur().isEmpty()) {
            utiliateur.setPassword_utilisateur(passwordEncoder.encode(details.getPassword_utilisateur()));
        }
        
        utiliateur.setTelephone_utilisateur(details.getTelephone_utilisateur());

        return utilisateurRepository.save(utiliateur);
    }
}
