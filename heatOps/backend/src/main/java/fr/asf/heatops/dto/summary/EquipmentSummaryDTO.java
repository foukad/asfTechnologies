package fr.asf.heatops.dto.summary;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class EquipmentSummaryDTO {
    private UUID id;
    private String name;
    private String brand;
}
