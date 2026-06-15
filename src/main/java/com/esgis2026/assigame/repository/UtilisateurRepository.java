package com.esgis2026.assigame.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.esgis2026.assigame.entity.Utilisateur;

import java.util.Optional;

@Repository
public interface UtilisateurRepository extends JpaRepository<Utilisateur, Long> {
    
    @Query("SELECT u FROM Utilisateur u WHERE u.Login_utilisateur = :login")
    Optional<Utilisateur> findByLogin_utilisateur(@Param("login") String login);
}
