package fr.asf.heatops.controller;

import fr.asf.heatops.domain.entity.Technician;
import fr.asf.heatops.dto.ApiErrorDTO;
import fr.asf.heatops.dto.intervention.InterventionResponseDTO;
import fr.asf.heatops.dto.technician.TechnicianRequestDTO;
import fr.asf.heatops.dto.technician.TechnicianResponseDTO;
import fr.asf.heatops.repository.TechnicianRepository;
import fr.asf.heatops.service.InterventionService;
import fr.asf.heatops.service.TechnicianService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
@Tag(name = "Technicians", description = "Gestion des techniciens et de leurs disponibilités")

@RestController
@RequestMapping("/technicians")
@RequiredArgsConstructor
@Tag(name = "Technicians", description = "API de gestion des techniciens")
public class TechnicianController {

    private final TechnicianService technicianService;
    private final InterventionService interventionService;
    private final TechnicianRepository technicianRepository;

    @Operation(summary = "Créer un technicien", description = "Crée un nouveau technicien",
            requestBody = @RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = TechnicianRequestDTO.class),
                            examples = @ExampleObject(value = "{\"firstName\": \"Marie\", \"lastName\": \"Martin\", \"phone\": \"0600000001\", \"email\": \"marie.martin@example.com\"}")
                    )
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Technicien créé",
                            content = @Content(schema = @Schema(implementation = TechnicianResponseDTO.class),
                                    examples = @ExampleObject(value = "{\"id\": \"00000000-0000-0000-0000-000000000000\", \"firstName\": \"Marie\", \"lastName\": \"Martin\", \"phone\": \"0600000001\", \"email\": \"marie.martin@example.com\"}")
                            )),
                    @ApiResponse(responseCode = "400", description = "Requête invalide",
                            content = @Content(schema = @Schema(implementation = ApiErrorDTO.class))),
                    @ApiResponse(responseCode = "500", description = "Erreur serveur",
                            content = @Content(schema = @Schema(implementation = ApiErrorDTO.class)))
            })
    @PostMapping
    public TechnicianResponseDTO create(@Valid @RequestBody TechnicianRequestDTO dto) {
        return technicianService.create(dto);
    }

    @Operation(summary = "Lister les techniciens", description = "Récupère tous les techniciens",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Liste des techniciens",
                            content = @Content(schema = @Schema(implementation = TechnicianResponseDTO.class)))
            })
    @GetMapping
    public List<TechnicianResponseDTO> findAll() {
        return technicianService.findAll();
    }

    @Operation(summary = "Récupérer un technicien", description = "Récupère un technicien par son identifiant",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Technicien trouvé",
                            content = @Content(schema = @Schema(implementation = TechnicianResponseDTO.class))),
                    @ApiResponse(responseCode = "404", description = "Technicien non trouvé",
                            content = @Content(schema = @Schema(implementation = ApiErrorDTO.class)))
            })
    @GetMapping("/{id}")
    public TechnicianResponseDTO findById(@PathVariable UUID id) {
        return technicianService.findById(id);
    }

    @GetMapping("/{id}/interventions")
    public List<InterventionResponseDTO> getTechnicianInterventions(@PathVariable UUID id) {
        return interventionService.findByTechnician(id);
    }

    @GetMapping("/search")
    public Page<TechnicianResponseDTO> search(
            @RequestParam(required = false) String search,
            Pageable pageable
    ) {
        return technicianService.search(search, pageable);
    }
    public void deactivate(UUID id) {
        Technician tech = technicianRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Technician not found"));

        tech.deactivate();
        technicianRepository.save(tech);
    }

}
