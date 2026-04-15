package fr.asf.heatops.dto.technician;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class TechnicianResponseDTO {
    private UUID id;
    private String firstName;
    private String lastName;
    private String phone;
    private String email;
}
