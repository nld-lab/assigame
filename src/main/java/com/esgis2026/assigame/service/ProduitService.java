package com.esgis2026.assigame.service;

import com.esgis2026.assigame.entity.CategorieProduit;
import com.esgis2026.assigame.entity.Produit;
import com.esgis2026.assigame.entity.Utilisateur;
import com.esgis2026.assigame.repository.CategorieProduitRepository;
import com.esgis2026.assigame.repository.ProduitRepository;
import com.esgis2026.assigame.repository.UtilisateurRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ProduitService {

    final ProduitRepository produitRepository;
    final UtilisateurRepository utilisateurRepository;
    final CategorieProduitRepository categorieProduitRepository;

    public ProduitService(
            ProduitRepository produitRepository,
            UtilisateurRepository utilisateurRepository,
            CategorieProduitRepository categorieProduitRepository) {
        this.produitRepository = produitRepository;
        this.utilisateurRepository = utilisateurRepository;
        this.categorieProduitRepository = categorieProduitRepository;
    }

    @Transactional(readOnly = true)
    public List<Produit> getAllProduits() {
        return produitRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Produit> getProduitsByCurrentUser() {
        Utilisateur user = getCurrentUser();
        return produitRepository.findByUtilisateurId(user.getId_utilisateur());
    }

    @Transactional(readOnly = true)
    public Produit getProduitById(Long id) {
        return produitRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("produit not found with id " + id));
    }

    @Transactional
    public Produit createProduit(Produit produit, MultipartFile image) throws IOException {
        produit.setId_produit(null);
        produit.setUtilisateur(getCurrentUser());
        produit.setDate_ajout(LocalDateTime.now());
        if (produit.getStatut() == null || produit.getStatut().isBlank()) {
            produit.setStatut("ACTIF");
        }
        if (produit.getCategorie_produit() == null
                || produit.getCategorie_produit().getIdcategorie_produit() == null) {
            throw new RuntimeException("La catégorie est obligatoire.");
        }
        produit.setCategorie_produit(resolveCategorie(produit.getCategorie_produit()));
        if (image == null || image.isEmpty()) {
            throw new RuntimeException("L'image du produit est obligatoire.");
        }
        produit.setImage_type(image.getContentType());
        produit.setImage(image.getBytes());
        return produitRepository.save(produit);
    }

    @Transactional
    public Produit updateProduit(Produit details, Long id, MultipartFile image) throws IOException {
        Produit produit = getManagedProduit(id);

        if (details.getNom_produit() != null) {
            produit.setNom_produit(details.getNom_produit());
        }
        if (details.getPrix() != null) {
            produit.setPrix(details.getPrix());
        }
        if (details.getDescription() != null) {
            produit.setDescription(details.getDescription());
        }
        if (details.getStatut() != null) {
            produit.setStatut(details.getStatut());
        }
        if (details.getCategorie_produit() != null) {
            produit.setCategorie_produit(resolveCategorie(details.getCategorie_produit()));
        }
        if (image != null && !image.isEmpty()) {
            produit.setImage_type(image.getContentType());
            produit.setImage(image.getBytes());
        }

        return produitRepository.save(produit);
    }

    public void deleteProduit(Long id) {
        Produit produit = getManagedProduit(id);
        produitRepository.delete(produit);
    }

    public Produit uploadImage(Long id, MultipartFile file) throws IOException {
        Produit produit = getManagedProduit(id);
        produit.setImage(file.getBytes());
        produit.setImage_type(file.getContentType());
        return produitRepository.save(produit);
    }

    private Produit getManagedProduit(Long id) {
        Produit produit = getProduitById(id);
        if (isAdmin()) {
            return produit;
        }
        Utilisateur currentUser = getCurrentUser();
        if (produit.getUtilisateur() == null
                || !produit.getUtilisateur().getId_utilisateur().equals(currentUser.getId_utilisateur())) {
            throw new RuntimeException("Vous n'êtes pas autorisé à modifier ce produit.");
        }
        return produit;
    }

    private boolean isAdmin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return false;
        }
        return authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch("ROLE_ADMIN"::equals);
    }

    private CategorieProduit resolveCategorie(CategorieProduit categorie) {
        if (categorie == null || categorie.getIdcategorie_produit() == null) {
            throw new RuntimeException("La catégorie est obligatoire.");
        }
        return categorieProduitRepository.findById(categorie.getIdcategorie_produit())
                .orElseThrow(() -> new RuntimeException(
                        "Catégorie introuvable avec l'id " + categorie.getIdcategorie_produit()));
    }

    private Utilisateur getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("Utilisateur non authentifié.");
        }
        return utilisateurRepository.findByLogin_utilisateur(authentication.getName())
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable."));
    }
}
