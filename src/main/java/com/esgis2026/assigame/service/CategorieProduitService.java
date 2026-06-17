package com.esgis2026.assigame.service;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

import com.esgis2026.assigame.entity.CategorieProduit;
import com.esgis2026.assigame.repository.CategorieProduitRepository;

@Service
public class CategorieProduitService {
    final CategorieProduitRepository categorieProduitRepository;

    public CategorieProduitService(CategorieProduitRepository categorieProduitRepository) {
        this.categorieProduitRepository = categorieProduitRepository;
    }

    public List<CategorieProduit> getAllCategorieProduits() {
        return categorieProduitRepository.findAll();
    }

    public CategorieProduit getCategorieProduitById(Long idCategorieProduit) {
        return categorieProduitRepository.findById(idCategorieProduit)
                .orElseThrow(() -> new RuntimeException("categorieProduit not found with id " + idCategorieProduit));
    }

    public CategorieProduit createCategorieProduit(CategorieProduit categorieProduit, MultipartFile image) throws IOException {
        categorieProduit.setIdcategorie_produit(null);
        if (image != null && !image.isEmpty()) {
            categorieProduit.setImage_type(image.getContentType());
            categorieProduit.setImage(image.getBytes());
        }
        return categorieProduitRepository.save(categorieProduit);
    }

    public CategorieProduit uploadImage(Long idCategorieProduit, MultipartFile file) throws IOException {
        CategorieProduit categorieProduit = getCategorieProduitById(idCategorieProduit);
        categorieProduit.setImage(file.getBytes());
        categorieProduit.setImage_type(file.getContentType());
        return categorieProduitRepository.save(categorieProduit);
    }

    public void deleteCategorieProduit(Long idCategorieProduit) {
        if (!categorieProduitRepository.existsById(idCategorieProduit)) {
            throw new RuntimeException("categorieProduit not found with id " + idCategorieProduit);
        }
        try {
            categorieProduitRepository.deleteById(idCategorieProduit);
        } catch (DataIntegrityViolationException e) {
            throw new RuntimeException(
                    "Impossible de supprimer cette catégorie : des produits y sont encore associés."
            );
        }
    }

    public CategorieProduit updateCategorieProduit(Long idCategorieProduit, CategorieProduit details, MultipartFile image) throws IOException {
        CategorieProduit categorieProduit = getCategorieProduitById(idCategorieProduit);

        if (details.getNom_categorieproduit() != null) {
            categorieProduit.setNom_categorieproduit(details.getNom_categorieproduit());
        }
        if (details.getDescription() != null) {
            categorieProduit.setDescription(details.getDescription());
        }
        if (image != null && !image.isEmpty()) {
            categorieProduit.setImage_type(image.getContentType());
            categorieProduit.setImage(image.getBytes());
        }

        return categorieProduitRepository.save(categorieProduit);
    }
}
