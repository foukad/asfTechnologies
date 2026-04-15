package fr.asf.heatops.controller;

import fr.asf.heatops.dto.ApiErrorDTO;
import fr.asf.heatops.dto.intervention.InterventionRequestDTO;
import fr.asf.heatops.dto.intervention.InterventionResponseDTO;
import fr.asf.heatops.dto.intervention.InterventionTimelineDTO;
import fr.asf.heatops.dto.intervention.InterventionUpdateRequestDTO;
import fr.asf.heatops.enums.InterventionStatus;
import fr.asf.heatops.service.InterventionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
@Tag(name = "Interventions", description = "Gestion des interventions terrain")

@RestController
@RequestMapping("/interventions")
@RequiredArgsConstructor
public class InterventionController {

    private final InterventionService interventionService;

    @Operation(summary = "Créer une intervention", description = "Crée une nouvelle intervention",
            requestBody = @RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = InterventionRequestDTO.class),
                            examples = @ExampleObject(value = "{\"title\": \"Remplacement vanne\", \"description\": \"Remplacement de la vanne principale\", \"scheduledAt\": \"2025-02-01T09:00:00\", \"clientId\": \"00000000-0000-0000-0000-000000000000\", \"equipmentId\": \"00000000-0000-0000-0000-000000000000\", \"technicianId\": \"00000000-0000-0000-0000-000000000000\"}")
                    )
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Intervention créée",
                            content = @Content(schema = @Schema(implementation = InterventionResponseDTO.class),
                                    examples = @ExampleObject(value = "{\"id\": \"00000000-0000-0000-0000-000000000000\", \"title\": \"Remplacement vanne\", \"description\": \"Remplacement de la vanne principale\", \"scheduledAt\": \"2025-02-01T09:00:00\"}")
                            )),
                    @ApiResponse(responseCode = "400", description = "Requête invalide",
                            content = @Content(schema = @Schema(implementation = ApiErrorDTO.class))),
                    @ApiResponse(responseCode = "500", description = "Erreur serveur",
                            content = @Content(schema = @Schema(implementation = ApiErrorDTO.class)))
            })
    @PostMapping
    public InterventionResponseDTO create(@Valid @RequestBody InterventionRequestDTO dto) {
        return interventionService.create(dto);
    }

    @Operation(summary = "Lister les interventions", description = "Récupère toutes les interventions",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Liste des interventions",
                            content = @Content(schema = @Schema(implementation = InterventionResponseDTO.class)))
            })
    @GetMapping
    public List<InterventionResponseDTO> findAll() {
        return interventionService.findAll();
    }

    @Operation(summary = "Récupérer une intervention", description = "Récupère une intervention par son identifiant",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Intervention trouvée",
                            content = @Content(schema = @Schema(implementation = InterventionResponseDTO.class))),
                    @ApiResponse(responseCode = "404", description = "Intervention non trouvée",
                            content = @Content(schema = @Schema(implementation = ApiErrorDTO.class)))
            })
    @GetMapping("/{id}")
    public InterventionResponseDTO findById(@PathVariable UUID id) {
        return interventionService.findById(id);
    }

    @Operation(summary = "Interventions par client", description = "Récupère les interventions pour un client donné",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Interventions du client",
                            content = @Content(schema = @Schema(implementation = InterventionResponseDTO.class)))
            })
    @GetMapping("/client/{clientId}")
    public List<InterventionResponseDTO> findByClient(@PathVariable UUID clientId) {
        return interventionService.findByClient(clientId);
    }

    @Operation(summary = "Interventions par équipement", description = "Récupère les interventions pour un équipement donné",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Interventions de l'équipement",
                            content = @Content(schema = @Schema(implementation = InterventionResponseDTO.class)))
            })
    @GetMapping("/equipment/{equipmentId}")
    public List<InterventionResponseDTO> findByEquipment(@PathVariable UUID equipmentId) {
        return interventionService.findByEquipment(equipmentId);
    }

    @Operation(summary = "Interventions par technicien", description = "Récupère les interventions pour un technicien donné",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Interventions du technicien",
                            content = @Content(schema = @Schema(implementation = InterventionResponseDTO.class)))
            })
    @GetMapping("/technician/{technicianId}")
    public List<InterventionResponseDTO> findByTechnician(@PathVariable UUID technicianId) {
        return interventionService.findByTechnician(technicianId);
    }

    @PostMapping("/{id}/assign")
    public InterventionResponseDTO assign(@PathVariable UUID id, @RequestParam UUID technicianId) {
        return interventionService.assignTechnician(id, technicianId);
    }

    @PostMapping("/{id}/start")
    public InterventionResponseDTO start(@PathVariable UUID id) {
        return interventionService.startIntervention(id);
    }

    @PostMapping("/{id}/complete")
    public InterventionResponseDTO complete(@PathVariable UUID id) {
        return interventionService.completeIntervention(id);
    }

    @GetMapping("/{id}/timeline")
    public InterventionTimelineDTO getTimeline(@PathVariable UUID id) {
        return interventionService.getTimeline(id);
    }

    @GetMapping("/search")
    public Page<InterventionResponseDTO> search(
            @RequestParam(required = false) InterventionStatus status,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to,
            Pageable pageable
    ) {
        return interventionService.search(status, from, to, pageable);
    }

    @PutMapping("/{id}")
    public InterventionResponseDTO update(
            @PathVariable UUID id,
            @RequestBody InterventionUpdateRequestDTO dto
    ) {
        return interventionService.update(id, dto);
    }
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deactivate(@PathVariable UUID id) {
        interventionService.deactivate(id);
    }

}
