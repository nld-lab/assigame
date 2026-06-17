package com.esgis2026.assigame.controller;

import com.esgis2026.assigame.entity.Produit;
import com.esgis2026.assigame.service.ProduitService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/produit")
public class ProduitController {
    final ProduitService produitService;

    public ProduitController(ProduitService produitService) {
        this.produitService = produitService;
    }

    @GetMapping
    public List<Produit> getAllProduit() {
        return produitService.getAllProduits();
    }

    @GetMapping("/mes-produits")
    public List<Produit> getMesProduits() {
        return produitService.getProduitsByCurrentUser();
    }

    @GetMapping("/{id}")
    public Produit getProduitById(@PathVariable Long id) {
        return produitService.getProduitById(id);
    }

    @GetMapping("/{id}/image")
    public ResponseEntity<byte[]> getImage(@PathVariable Long id) {
        try {
            Produit produit = produitService.getProduitById(id);
            if (produit.getImage() == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok()
                    .header("Content-Type", produit.getImage_type() != null
                            ? produit.getImage_type()
                            : "application/octet-stream")
                    .body(produit.getImage());
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping(value = "/add", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createProduit(
            @RequestPart("produit") Produit produit,
            @RequestPart("image") MultipartFile image) {
        try {
            Produit created = produitService.createProduit(produit, image);
            return new ResponseEntity<>(created, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteProduit(@PathVariable Long id) {
        try {
            produitService.deleteProduit(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            if (e.getMessage() != null && e.getMessage().contains("autorisé")) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
            }
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping(value = "/update/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateProduitJson(@RequestBody Produit produit, @PathVariable Long id) {
        try {
            Produit updated = produitService.updateProduit(produit, id, null);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            if (e.getMessage() != null && e.getMessage().contains("autorisé")) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
            }
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping(value = "/update/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateProduitMultipart(
            @PathVariable Long id,
            @RequestPart("produit") Produit produit,
            @RequestPart(value = "image", required = false) MultipartFile image) {
        try {
            Produit updated = produitService.updateProduit(produit, id, image);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            if (e.getMessage() != null && e.getMessage().contains("autorisé")) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
            }
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
