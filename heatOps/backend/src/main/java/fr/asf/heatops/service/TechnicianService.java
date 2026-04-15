package fr.asf.heatops.service;

import fr.asf.heatops.domain.entity.Technician;
import fr.asf.heatops.dto.technician.TechnicianRequestDTO;
import fr.asf.heatops.dto.technician.TechnicianResponseDTO;
import fr.asf.heatops.mapper.TechnicianMapper;
import fr.asf.heatops.repository.TechnicianRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TechnicianService {

    private final TechnicianRepository technicianRepository;
    private final TechnicianMapper technicianMapper;

    public TechnicianResponseDTO create(TechnicianRequestDTO dto) {
        Technician technician = technicianMapper.toEntity(dto);
        technician = technicianRepository.save(technician);
        return technicianMapper.toResponse(technician);
    }

    public List<TechnicianResponseDTO> findAll() {
        return technicianRepository.findAll()
                .stream()
                .map(technicianMapper::toResponse)
                .toList();
    }

    public TechnicianResponseDTO findById(UUID id) {
        Technician technician = technicianRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Technician not found"));
        return technicianMapper.toResponse(technician);
    }

    public void delete(UUID id) {
        technicianRepository.deleteById(id);
    }

    public Page<TechnicianResponseDTO> search(String search, Pageable pageable) {
        return technicianRepository.search(search, pageable)
                .map(technicianMapper::toResponse);
    }

}
