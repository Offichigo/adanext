package fr.adatechschool.adanext.controller;

import fr.adatechschool.adanext.dto.request.RegisterRequest;
import fr.adatechschool.adanext.dto.response.UserResponse;
import fr.adatechschool.adanext.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentification", description = "Inscription et connexion")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    @Operation(summary = "Inscrire un nouvel utilisateur")
    public ResponseEntity<UserResponse> register(@Valid @RequestBody RegisterRequest request) {
        UserResponse user = authService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    /*
     * La route POST /api/auth/login est geree directement par Spring Security.
     * Elle attend les parametres "email" et "password" en form-data ou JSON.
     * Voir SecurityConfig pour la configuration du succes/echec.
     */
}
