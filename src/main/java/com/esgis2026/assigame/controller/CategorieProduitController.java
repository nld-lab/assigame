package com.esgis2026.assigame.controller;

import com.esgis2026.assigame.entity.CategorieProduit;
import com.esgis2026.assigame.service.CategorieProduitService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/categorieproduit")
public class CategorieProduitController {
    private final CategorieProduitService categorieProduitService;

    public CategorieProduitController(CategorieProduitService categorieProduitService) {
        this.categorieProduitService = categorieProduitService;
    }

    @GetMapping("/list")
    public List<CategorieProduit> getAllCategorieProduits() {
        return categorieProduitService.getAllCategorieProduits();
    }

    @GetMapping("/{id}/image")
    public ResponseEntity<byte[]> getImage(@PathVariable Long id) {
        try {
            CategorieProduit categorieProduit = categorieProduitService.getCategorieProduitById(id);
            if (categorieProduit.getImage() == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok()
                    .header("Content-Type", categorieProduit.getImage_type() != null
                            ? categorieProduit.getImage_type()
                            : "application/octet-stream")
                    .body(categorieProduit.getImage());
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping(value = "/add", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addCategorieProduitJson(@RequestBody CategorieProduit categorieProduit) {
        try {
            CategorieProduit created = categorieProduitService.createCategorieProduit(categorieProduit, null);
            return new ResponseEntity<>(created, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(value = "/add", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> addCategorieProduitMultipart(
            @RequestPart("categorie") CategorieProduit categorieProduit,
            @RequestPart(value = "image", required = false) MultipartFile image) {
        try {
            CategorieProduit created = categorieProduitService.createCategorieProduit(categorieProduit, image);
            return new ResponseEntity<>(created, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/{id}/upload-image")
    public ResponseEntity<?> uploadImage(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file) {
        try {
            if (file.isEmpty()) {
                return ResponseEntity.badRequest().body("Le fichier image est vide.");
            }
            CategorieProduit updated = categorieProduitService.uploadImage(id, file);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteCategorieProduit(@PathVariable Long id) {
        try {
            categorieProduitService.deleteCategorieProduit(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            if (e.getMessage() != null && e.getMessage().contains("Impossible de supprimer")) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
            }
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping(value = "/update/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateCategorieProduitJson(
            @PathVariable Long id,
            @RequestBody CategorieProduit categorieProduit) {
        try {
            CategorieProduit updated = categorieProduitService.updateCategorieProduit(id, categorieProduit, null);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping(value = "/update/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateCategorieProduitMultipart(
            @PathVariable Long id,
            @RequestPart("categorie") CategorieProduit categorieProduit,
            @RequestPart(value = "image", required = false) MultipartFile image) {
        try {
            CategorieProduit updated = categorieProduitService.updateCategorieProduit(id, categorieProduit, image);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
