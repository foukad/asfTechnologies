package fr.asf.heatops.dto.technician;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TechnicianRequestDTO {

    @NotBlank(message = "Le prénom est obligatoire")
    private String firstName;

    @NotBlank(message = "Le nom est obligatoire")
    private String lastName;

    @NotBlank(message = "Le téléphone est obligatoire")
    private String phone;

    @Email(message = "Email invalide")
    private String email;
}

