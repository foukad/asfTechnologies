package fr.asf.heatops.dto.client;

import fr.asf.heatops.dto.summary.EquipmentSummaryDTO;
import fr.asf.heatops.dto.summary.InterventionSummaryDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class ClientResponseDTO {
    private UUID id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;

    private List<EquipmentSummaryDTO> equipment;
    private List<InterventionSummaryDTO> interventions;
}

