package fr.asf.heatops.mapper;

import fr.asf.heatops.domain.entity.Client;
import fr.asf.heatops.domain.entity.Equipment;
import fr.asf.heatops.domain.entity.Intervention;
import fr.asf.heatops.dto.client.ClientRequestDTO;
import fr.asf.heatops.dto.client.ClientResponseDTO;
import fr.asf.heatops.dto.summary.EquipmentSummaryDTO;
import fr.asf.heatops.dto.summary.InterventionSummaryDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ClientMapper {

    @Mapping(target = "equipment", source = "equipmentList")
    @Mapping(target = "interventions", source = "interventions")
    ClientResponseDTO toResponse(Client entity);

    Client toEntity(ClientRequestDTO dto);

    EquipmentSummaryDTO toEquipmentSummary(Equipment equipment);

    InterventionSummaryDTO toInterventionSummary(Intervention intervention);
}
