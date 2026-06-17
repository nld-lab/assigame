package com.esgis2026.assigame.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Objects;

@Entity
@Getter
@Setter
@Table(name = "produit")
public class Produit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id_produit;

    @Column( nullable = false, length = 50)
    private String nom_produit;

    @Column( length = 200)
    private String description;

    @Column()
    private Double prix;

    @JsonIgnore
    @Column(columnDefinition = "BYTEA")
    private byte[] image;

    @Column(length = 100)
    private String image_type;

    @Column
    private LocalDateTime date_ajout;

    @Column(nullable = false)
    private String statut;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idcategorie_produit")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "image"})
    private CategorieProduit categorie_produit;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_utilisateur")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "password_utilisateur", "Password_utilisateur"})
    private Utilisateur utilisateur;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Produit produit = (Produit) o;
        return Objects.equals(id_produit, produit.id_produit);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id_produit);
    }

    @Override
    public String toString() {
        return "Produit{" +
                "id_produit=" + id_produit +
                ", nom_produit='" + nom_produit + '\'' +
                ", description='" + description + '\'' +
                ", prix=" + prix +
                ", image=" + Arrays.toString(image) +
                ", image_type='" + image_type + '\'' +
                ", date_ajout=" + date_ajout +
                ", statut='" + statut + '\'' +
                ", categorie_produit=" + categorie_produit +
                ", utilisateur=" + utilisateur +
                '}';
    }
}
