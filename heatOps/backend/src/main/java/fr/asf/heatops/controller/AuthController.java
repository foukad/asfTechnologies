package fr.asf.heatops.controller;

import fr.asf.heatops.dto.auth.LoginRequestDTO;
import fr.asf.heatops.dto.auth.RegisterRequestDTO;
import fr.asf.heatops.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
@Tag(name = "Auth", description = "Authentification et gestion du JWT")

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    @Operation(
            summary = "Authentification utilisateur",
            description = "Retourne un JWT valide pour accéder aux endpoints protégés."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Authentification réussie"),
            @ApiResponse(responseCode = "401", description = "Identifiants invalides")
    })
    @PostMapping("/login")
    public String login(@Valid @RequestBody LoginRequestDTO dto) {
        return authService.login(dto.getEmail(), dto.getPassword());
    }

    @PostMapping("/register")
    public String register(@Valid @RequestBody RegisterRequestDTO dto) {
        return authService.register(dto.getCompany(), dto.getEmail(), dto.getPassword());
    }
}
