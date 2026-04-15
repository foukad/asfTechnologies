package fr.asf.heatops.dto.equipment;

import fr.asf.heatops.dto.summary.ClientSummaryDTO;
import fr.asf.heatops.dto.summary.InterventionSummaryDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class EquipmentResponseDTO {
    private UUID id;
    private String name;
    private String brand;
    private String model;
    private String serialNumber;
    private Integer year;

    private ClientSummaryDTO client;
    private List<InterventionSummaryDTO> interventions;
}

