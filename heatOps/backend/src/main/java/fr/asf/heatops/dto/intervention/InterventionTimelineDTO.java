package fr.asf.heatops.dto.intervention;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;

public record InterventionTimelineDTO(
        UUID id,
        LocalDateTime scheduledAt,
        LocalDateTime startedAt,
        LocalDateTime completedAt,
        Long durationMinutes
) {}
