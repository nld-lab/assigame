package com.esgis2026.assigame.controller;

import com.esgis2026.assigame.dto.AuthResponse;
import com.esgis2026.assigame.dto.LoginRequest;
import com.esgis2026.assigame.dto.UpdateProfileRequest;
import com.esgis2026.assigame.dto.UtilisateurDto;
import com.esgis2026.assigame.entity.Utilisateur;
import com.esgis2026.assigame.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationService service;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody Utilisateur request) {
        return ResponseEntity.ok(service.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(service.login(request));
    }

    @GetMapping("/me")
    public ResponseEntity<UtilisateurDto> me(Authentication authentication) {
        if (authentication == null
                || !authentication.isAuthenticated()
                || "anonymousUser".equals(String.valueOf(authentication.getPrincipal()))) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.ok(service.getCurrentUser(authentication.getName()));
    }

    @PutMapping("/me")
    public ResponseEntity<UtilisateurDto> updateMe(
            Authentication authentication,
            @RequestBody UpdateProfileRequest request
    ) {
        if (authentication == null
                || !authentication.isAuthenticated()
                || "anonymousUser".equals(String.valueOf(authentication.getPrincipal()))) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.ok(service.updateCurrentUser(authentication.getName(), request));
    }
}
