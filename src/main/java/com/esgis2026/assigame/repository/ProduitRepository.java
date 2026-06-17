package com.esgis2026.assigame.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.esgis2026.assigame.entity.Produit;

import java.util.List;

@Repository
public interface ProduitRepository extends JpaRepository<Produit, Long> {

    @Query("SELECT p FROM Produit p WHERE p.utilisateur.id_utilisateur = :idUtilisateur")
    List<Produit> findByUtilisateurId(@Param("idUtilisateur") Long idUtilisateur);
}
