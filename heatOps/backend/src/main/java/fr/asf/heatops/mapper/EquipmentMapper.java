package fr.asf.heatops.mapper;

import fr.asf.heatops.domain.entity.Client;
import fr.asf.heatops.domain.entity.Equipment;
import fr.asf.heatops.domain.entity.Intervention;
import fr.asf.heatops.dto.equipment.EquipmentRequestDTO;
import fr.asf.heatops.dto.equipment.EquipmentResponseDTO;
import fr.asf.heatops.dto.summary.ClientSummaryDTO;
import fr.asf.heatops.dto.summary.InterventionSummaryDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface EquipmentMapper {

    @Mapping(target = "client", source = "client")
    @Mapping(target = "interventions", source = "interventions")
    EquipmentResponseDTO toResponse(Equipment entity);

    Equipment toEntity(EquipmentRequestDTO dto);

    ClientSummaryDTO toClientSummary(Client client);

    InterventionSummaryDTO toInterventionSummary(Intervention intervention);
}

