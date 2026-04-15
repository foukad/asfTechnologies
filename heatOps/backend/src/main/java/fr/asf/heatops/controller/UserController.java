package fr.asf.heatops.controller;

import fr.asf.heatops.domain.entity.User;
import fr.asf.heatops.dto.ApiErrorDTO;
import fr.asf.heatops.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.ExampleObject;
@Tag(name = "Users", description = "Gestion des utilisateurs internes HeatOps")

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Tag(name = "Users", description = "API des utilisateurs et informations du compte courant")
public class UserController {

    private final UserService userService;

    @Operation(summary = "Utilisateur courant", description = "Récupère les informations de l'utilisateur connecté",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Utilisateur courant",
                            content = @Content(schema = @Schema(implementation = User.class),
                                    examples = @ExampleObject(value = "{\"id\": \"00000000-0000-0000-0000-000000000000\", \"username\": \"jdupont\", \"roles\": [\"ROLE_USER\"]}")
                            )),
                    @ApiResponse(responseCode = "401", description = "Non authentifié",
                            content = @Content(schema = @Schema(implementation = ApiErrorDTO.class))),
                    @ApiResponse(responseCode = "500", description = "Erreur serveur",
                            content = @Content(schema = @Schema(implementation = ApiErrorDTO.class)))
            })
    @GetMapping("/me")
    public User me() {
        return userService.getCurrentUser();
    }
}
