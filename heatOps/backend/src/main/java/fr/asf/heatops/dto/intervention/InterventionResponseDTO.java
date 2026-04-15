package fr.asf.heatops.dto.intervention;

import fr.asf.heatops.dto.summary.ClientSummaryDTO;
import fr.asf.heatops.dto.summary.EquipmentSummaryDTO;
import fr.asf.heatops.dto.summary.TechnicianSummaryDTO;
import fr.asf.heatops.enums.InterventionStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class InterventionResponseDTO {
    private UUID id;
    private String title;
    private String description;
    private LocalDateTime scheduledAt;
    private InterventionStatus status;
    private LocalDateTime startedAt;
    private LocalDateTime completedAt;
    private ClientSummaryDTO client;
    private EquipmentSummaryDTO equipment;
    private TechnicianSummaryDTO technician;
}

