package fr.asf.heatops.dto.summary;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class InterventionSummaryDTO {
    private UUID id;
    private String title;
    private LocalDateTime scheduledAt;
}
