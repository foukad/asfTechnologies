package fr.asf.heatops.mapper;

import fr.asf.heatops.domain.entity.Technician;
import fr.asf.heatops.dto.technician.TechnicianRequestDTO;
import fr.asf.heatops.dto.technician.TechnicianResponseDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TechnicianMapper {

    Technician toEntity(TechnicianRequestDTO dto);

    TechnicianResponseDTO toResponse(Technician entity);
}
