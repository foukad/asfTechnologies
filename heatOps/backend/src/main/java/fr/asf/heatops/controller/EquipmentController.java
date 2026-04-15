package fr.asf.heatops.controller;

import fr.asf.heatops.dto.ApiErrorDTO;
import fr.asf.heatops.dto.equipment.EquipmentRequestDTO;
import fr.asf.heatops.dto.equipment.EquipmentResponseDTO;
import fr.asf.heatops.service.EquipmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
@Tag(name = "Equipments", description = "Gestion des équipements (chaudières, PAC, etc.)")

@RestController
@RequestMapping("/equipment")
@RequiredArgsConstructor
public class EquipmentController {

    private final EquipmentService equipmentService;

    @Operation(summary = "Créer un équipement pour un client", description = "Crée un équipement lié à un client",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = EquipmentRequestDTO.class),
                            examples = @ExampleObject(value = "{\"name\": \"Chaudière X200\", \"brand\": \"BrandCo\", \"model\": \"X200\", \"serialNumber\": \"SN123456\", \"year\": 2020}")
                    )
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Équipement créé",
                            content = @Content(schema = @Schema(implementation = EquipmentResponseDTO.class),
                                    examples = @ExampleObject(value = "{\"id\": \"00000000-0000-0000-0000-000000000000\", \"name\": \"Chaudière X200\", \"brand\": \"BrandCo\", \"model\": \"X200\", \"serialNumber\": \"SN123456\", \"year\": 2020}")
                            )),
                    @ApiResponse(responseCode = "400", description = "Requête invalide",
                            content = @Content(schema = @Schema(implementation = ApiErrorDTO.class),
                                    examples = @ExampleObject(value = "{\"message\": \"Le nom de l'équipement est obligatoire\", \"error\": \"Bad Request\", \"timestamp\": \"2025-01-01T12:00:00\"}")) ),
                    @ApiResponse(responseCode = "500", description = "Erreur serveur",
                            content = @Content(schema = @Schema(implementation = ApiErrorDTO.class)))
            })
    @PostMapping("/client/{clientId}")
    public EquipmentResponseDTO create(@Parameter(description = "ID du client") @PathVariable UUID clientId,
            @Valid @RequestBody EquipmentRequestDTO dto
    ) {
        return equipmentService.create(clientId, dto);
    }

    @Operation(summary = "Lister les équipements", description = "Récupère tous les équipements",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Liste des équipements",
                            content = @Content(schema = @Schema(implementation = EquipmentResponseDTO.class)))
            })
    @GetMapping
    public List<EquipmentResponseDTO> findAll() {
        return equipmentService.findAll();
    }

    @Operation(summary = "Récupérer un équipement", description = "Récupère un équipement par son identifiant",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Équipement trouvé",
                            content = @Content(schema = @Schema(implementation = EquipmentResponseDTO.class),
                                    examples = @ExampleObject(value = "{\"id\": \"00000000-0000-0000-0000-000000000000\", \"name\": \"Chaudière X200\", \"brand\": \"BrandCo\", \"model\": \"X200\", \"serialNumber\": \"SN123456\", \"year\": 2020}")
                            )),
                    @ApiResponse(responseCode = "404", description = "Équipement non trouvé",
                            content = @Content(schema = @Schema(implementation = ApiErrorDTO.class),
                                    examples = @ExampleObject(value = "{\"message\": \"Équipement non trouvé\", \"error\": \"Not Found\", \"timestamp\": \"2025-01-01T12:00:00\"}")))
            })
    @GetMapping("/{id}")
    public EquipmentResponseDTO findById(@PathVariable UUID id) {
        return equipmentService.findById(id);
    }

    @Operation(summary = "Récupérer les équipements d'un client", description = "Liste les équipements d'un client donné",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Équipements du client",
                            content = @Content(schema = @Schema(implementation = EquipmentResponseDTO.class)))
            })
    @GetMapping("/client/{clientId}")
    public List<EquipmentResponseDTO> findByClient(@PathVariable UUID clientId) {
        return equipmentService.findByClient(clientId);
    }

    @GetMapping("/search")
    public Page<EquipmentResponseDTO> search(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) UUID clientId,
            @RequestParam(required = false) List<String> models,
            @RequestParam(required = false) List<String> brands,
            Pageable pageable
    ) {
        return equipmentService.search(search, clientId, models, brands, pageable);
    }
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deactivate(@PathVariable UUID id) {
        equipmentService.deactivate(id);
    }

}
