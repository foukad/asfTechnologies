package fr.asf.heatops.controller;

import fr.asf.heatops.domain.entity.Equipment;
import fr.asf.heatops.dto.ApiErrorDTO;
import fr.asf.heatops.dto.client.ClientRequestDTO;
import fr.asf.heatops.dto.client.ClientResponseDTO;
import fr.asf.heatops.dto.equipment.EquipmentResponseDTO;
import fr.asf.heatops.dto.intervention.InterventionResponseDTO;
import fr.asf.heatops.service.ClientService;
import fr.asf.heatops.service.EquipmentService;
import fr.asf.heatops.service.InterventionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
@Tag(name = "Clients", description = "Gestion des clients et de leurs informations")

@RestController
@RequestMapping("/clients")
@RequiredArgsConstructor
@Tag(name = "Clients", description = "API de gestion des clients")
public class ClientController {

    private final ClientService clientService;
    private final EquipmentService equipmentService;
    private final InterventionService interventionService;

    @Operation(summary = "Créer un client", description = "Crée un nouveau client",
            requestBody = @RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ClientRequestDTO.class),
                            examples = @ExampleObject(value = "{\"firstName\": \"Jean\", \"lastName\": \"Dupont\", \"email\": \"jean.dupont@example.com\", \"phone\": \"0600000000\"}")
                    )
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Client créé",
                            content = @Content(schema = @Schema(implementation = ClientResponseDTO.class),
                                    examples = @ExampleObject(value = "{\"id\": \"00000000-0000-0000-0000-000000000000\", \"firstName\": \"Jean\", \"lastName\": \"Dupont\", \"email\": \"jean.dupont@example.com\", \"phone\": \"0600000000\"}")
                            )),
                    @ApiResponse(responseCode = "400", description = "Requête invalide",
                            content = @Content(schema = @Schema(implementation = ApiErrorDTO.class))),
                    @ApiResponse(responseCode = "500", description = "Erreur serveur",
                            content = @Content(schema = @Schema(implementation = ApiErrorDTO.class)))
            })
    @PostMapping
    public ClientResponseDTO create(@Valid @RequestBody ClientRequestDTO dto) {
        return clientService.create(dto);
    }

    @Operation(summary = "Lister les clients", description = "Récupère une page de clients",
            parameters = {
                    @Parameter(name = "page", description = "Numéro de page (0..N)"),
                    @Parameter(name = "size", description = "Taille de la page"),
                    @Parameter(name = "sort", description = "Tri, ex: firstName,asc")
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Page de clients",
                            content = @Content(schema = @Schema(implementation = ClientResponseDTO.class)))
            })
    @GetMapping
    public Page<ClientResponseDTO> findAll(Pageable pageable) {
        return clientService.findAll(pageable);
    }

    @Operation(summary = "Récupérer un client", description = "Récupère un client par son identifiant",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Client trouvé",
                            content = @Content(schema = @Schema(implementation = ClientResponseDTO.class))),
                    @ApiResponse(responseCode = "404", description = "Client non trouvé",
                            content = @Content(schema = @Schema(implementation = ApiErrorDTO.class)))
            })
    @GetMapping("/{id}")
    public ClientResponseDTO findById(@PathVariable UUID id) {
        return clientService.findById(id);
    }

    @Operation(summary = "Rechercher des clients", description = "Recherche des clients par nom et/ou email",
            parameters = {
                    @Parameter(name = "name", description = "Nom ou prénom à rechercher"),
                    @Parameter(name = "email", description = "Email à rechercher"),
                    @Parameter(name = "page", description = "Numéro de page (0..N)"),
                    @Parameter(name = "size", description = "Taille de la page"),
                    @Parameter(name = "sort", description = "Tri, ex: firstName,asc")
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Résultats de la recherche",
                            content = @Content(schema = @Schema(implementation = ClientResponseDTO.class)))
            })
    @GetMapping("/search")
    public Page<ClientResponseDTO> search(
            @RequestParam(required = false) String search,
            Pageable pageable
    ) {
        return clientService.search(search, pageable);
    }

    @GetMapping("/{id}/equipments")
    public List<EquipmentResponseDTO> getClientEquipments(@PathVariable UUID id) {
        return equipmentService.findByClient(id);
    }

    @GetMapping("/{id}/interventions")
    public List<InterventionResponseDTO> getClientInterventions(@PathVariable UUID id) {
        return interventionService.findByClient(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deactivate(@PathVariable UUID id) {
        clientService.deactivate(id);
    }

}
