package fr.asf.heatops.dto.intervention;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class InterventionRequestDTO {

    @NotBlank(message = "Le titre est obligatoire")
    private String title;

    @NotBlank(message = "La description est obligatoire")
    private String description;

    @NotNull(message = "La date est obligatoire")
    private LocalDateTime scheduledAt;

    @NotNull(message = "Le client est obligatoire")
    private UUID clientId;

    @NotNull(message = "L'équipement est obligatoire")
    private UUID equipmentId;

    @NotNull(message = "Le technicien est obligatoire")
    private UUID technicianId;
}

