package com.esgis2026.assigame.dto;

import com.esgis2026.assigame.entity.Utilisateur;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UtilisateurDto {

    private Long id_utilisateur;
    private String nom_utilisateur;
    private String prenom_utilisateur;
    private String sexe_utilisateur;
    private String telephone_utilisateur;
    private String mail_utilisateur;
    private String login_utilisateur;
    private String residence_utilisateur;
    private Long id_type_utilisateur;
    private String role;

    public static UtilisateurDto fromEntity(Utilisateur u) {
        String role = null;
        Long typeId = null;
        if (u.getType_utilisateur() != null) {
            typeId = u.getType_utilisateur().getId_type_utilisateur();
            if (u.getType_utilisateur().getLibelle_type_utilisateur() != null) {
                role = u.getType_utilisateur().getLibelle_type_utilisateur().toUpperCase();
            }
        }
        return UtilisateurDto.builder()
                .id_utilisateur(u.getId_utilisateur())
                .nom_utilisateur(u.getNom_utilisateur())
                .prenom_utilisateur(u.getPrenom_utilisateur())
                .sexe_utilisateur(u.getSexe_utilisateur())
                .telephone_utilisateur(u.getTelephone_utilisateur())
                .mail_utilisateur(u.getMail_utilisateur())
                .login_utilisateur(u.getLogin_utilisateur())
                .residence_utilisateur(u.getResidence_utilisateur())
                .id_type_utilisateur(typeId)
                .role(role)
                .build();
    }
}
