package com.esgis2026.assigame.service;

import com.esgis2026.assigame.dto.UpdateProfileRequest;
import com.esgis2026.assigame.entity.TypeUtilisateur;
import com.esgis2026.assigame.entity.Utilisateur;
import com.esgis2026.assigame.repository.TypeUtilisateurRepository;
import com.esgis2026.assigame.repository.UtilisateurRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UtilisateurService {
    final UtilisateurRepository utilisateurRepository;
    final TypeUtilisateurRepository typeUtilisateurRepository;
    final PasswordEncoder passwordEncoder;

    public UtilisateurService(
            UtilisateurRepository utilisateurRepository,
            TypeUtilisateurRepository typeUtilisateurRepository,
            PasswordEncoder passwordEncoder) {
        this.utilisateurRepository = utilisateurRepository;
        this.typeUtilisateurRepository = typeUtilisateurRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<Utilisateur> getAllUtilisateurs() {
        return utilisateurRepository.findAll();
    }

    public Utilisateur createUtilisateur(Utilisateur utilisateur) {
        utilisateur.setPassword_utilisateur(passwordEncoder.encode(utilisateur.getPassword_utilisateur()));
        if (utilisateur.getType_utilisateur() != null) {
            utilisateur.setType_utilisateur(resolveType(utilisateur.getType_utilisateur()));
        }
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

        if (details.getType_utilisateur() != null) {
            utiliateur.setType_utilisateur(resolveType(details.getType_utilisateur()));
        }

        return utilisateurRepository.save(utiliateur);
    }

    public Utilisateur updateProfile(String login, UpdateProfileRequest request) {
        Utilisateur utilisateur = utilisateurRepository.findByLogin_utilisateur(login)
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));

        utilisateur.setNom_utilisateur(request.getNom_utilisateur());
        utilisateur.setPrenom_utilisateur(request.getPrenom_utilisateur());
        utilisateur.setSexe_utilisateur(request.getSexe_utilisateur());
        utilisateur.setTelephone_utilisateur(request.getTelephone_utilisateur());
        utilisateur.setMail_utilisateur(request.getMail_utilisateur());
        utilisateur.setResidence_utilisateur(request.getResidence_utilisateur());

        if (request.getPassword_utilisateur() != null && !request.getPassword_utilisateur().isBlank()) {
            utilisateur.setPassword_utilisateur(passwordEncoder.encode(request.getPassword_utilisateur()));
        }

        return utilisateurRepository.save(utilisateur);
    }

    private TypeUtilisateur resolveType(TypeUtilisateur type) {
        if (type == null || type.getId_type_utilisateur() == null) {
            throw new RuntimeException("Le type d'utilisateur est obligatoire.");
        }
        return typeUtilisateurRepository.findById(type.getId_type_utilisateur())
                .orElseThrow(() -> new RuntimeException(
                        "Type d'utilisateur introuvable avec l'id " + type.getId_type_utilisateur()));
    }
}
