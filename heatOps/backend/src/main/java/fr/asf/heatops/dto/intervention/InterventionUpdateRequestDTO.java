package fr.asf.heatops.dto.intervention;

import java.time.LocalDateTime;
import java.util.UUID;

public record InterventionUpdateRequestDTO(
        String title,
        String description,
        LocalDateTime scheduledAt,
        UUID equipmentId,
        UUID technicianId
) {}
