package fr.asf.heatops.dto.equipment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EquipmentRequestDTO {

    @NotBlank(message = "Le nom de l'équipement est obligatoire")
    private String name;

    @NotBlank(message = "La marque est obligatoire")
    private String brand;

    @NotBlank(message = "Le modèle est obligatoire")
    private String model;

    @NotBlank(message = "Le numéro de série est obligatoire")
    private String serialNumber;

    @NotNull(message = "L'année est obligatoire")
    @Positive(message = "L'année doit être positive")
    private Integer year;
}
