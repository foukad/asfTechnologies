package fr.asf.heatops.mapper;

import fr.asf.heatops.domain.entity.Client;
import fr.asf.heatops.domain.entity.Equipment;
import fr.asf.heatops.domain.entity.Intervention;
import fr.asf.heatops.domain.entity.Technician;
import fr.asf.heatops.dto.intervention.InterventionRequestDTO;
import fr.asf.heatops.dto.intervention.InterventionResponseDTO;
import fr.asf.heatops.dto.summary.ClientSummaryDTO;
import fr.asf.heatops.dto.summary.EquipmentSummaryDTO;
import fr.asf.heatops.dto.summary.TechnicianSummaryDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface InterventionMapper {

    @Mapping(target = "client", source = "client")
    @Mapping(target = "equipment", source = "equipment")
    @Mapping(target = "technician", source = "technician")
    InterventionResponseDTO toResponse(Intervention entity);

    ClientSummaryDTO toClientSummary(Client client);
    EquipmentSummaryDTO toEquipmentSummary(Equipment equipment);
    TechnicianSummaryDTO toTechnicianSummary(Technician technician);
}

