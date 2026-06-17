package com.esgis2026.assigame.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateProfileRequest {
    private String nom_utilisateur;
    private String prenom_utilisateur;
    private String sexe_utilisateur;
    private String telephone_utilisateur;
    private String mail_utilisateur;
    private String residence_utilisateur;
    private String password_utilisateur;
}
