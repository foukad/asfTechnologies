package fr.asf.heatops.service;

import fr.asf.heatops.domain.entity.Client;
import fr.asf.heatops.domain.entity.Equipment;
import fr.asf.heatops.domain.entity.Intervention;
import fr.asf.heatops.domain.entity.Technician;
import fr.asf.heatops.dto.intervention.InterventionRequestDTO;
import fr.asf.heatops.dto.intervention.InterventionResponseDTO;
import fr.asf.heatops.dto.intervention.InterventionTimelineDTO;
import fr.asf.heatops.dto.intervention.InterventionUpdateRequestDTO;
import fr.asf.heatops.enums.InterventionStatus;
import fr.asf.heatops.mapper.InterventionMapper;
import fr.asf.heatops.repository.ClientRepository;
import fr.asf.heatops.repository.EquipmentRepository;
import fr.asf.heatops.repository.InterventionRepository;
import fr.asf.heatops.repository.TechnicianRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static fr.asf.heatops.enums.InterventionStatus.*;

@Service
@RequiredArgsConstructor
public class InterventionService {

    private final InterventionRepository interventionRepository;
    private final ClientRepository clientRepository;
    private final EquipmentRepository equipmentRepository;
    private final TechnicianRepository technicianRepository;
    private final InterventionMapper interventionMapper;

    public InterventionResponseDTO create(InterventionRequestDTO dto) {

        Client client = clientRepository.findById(dto.getClientId())
                .orElseThrow(() -> new RuntimeException("Client not found"));

        Equipment equipment = equipmentRepository.findById(dto.getEquipmentId())
                .orElseThrow(() -> new RuntimeException("Equipment not found"));

        Technician technician = technicianRepository.findById(dto.getTechnicianId())
                .orElseThrow(() -> new RuntimeException("Technician not found"));

        Intervention intervention = new Intervention();
        intervention.setTitle(dto.getTitle());
        intervention.setDescription(dto.getDescription());
        intervention.setScheduledAt(dto.getScheduledAt());
        intervention.setClient(client);
        intervention.setEquipment(equipment);
        intervention.setTechnician(technician);

        intervention = interventionRepository.save(intervention);

        return interventionMapper.toResponse(intervention);
    }

    public List<InterventionResponseDTO> findAll() {
        return interventionRepository.findAll()
                .stream()
                .map(interventionMapper::toResponse)
                .toList();
    }

    public InterventionResponseDTO findById(UUID id) {
        Intervention intervention = interventionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Intervention not found"));
        return interventionMapper.toResponse(intervention);
    }

    public List<InterventionResponseDTO> findByClient(UUID clientId) {
        return interventionRepository.findByClientId(clientId)
                .stream()
                .map(interventionMapper::toResponse)
                .toList();
    }

    public List<InterventionResponseDTO> findByEquipment(UUID equipmentId) {
        return interventionRepository.findByEquipmentId(equipmentId)
                .stream()
                .map(interventionMapper::toResponse)
                .toList();
    }

    public List<InterventionResponseDTO> findByTechnician(UUID technicianId) {
        return interventionRepository.findByTechnicianId(technicianId)
                .stream()
                .map(interventionMapper::toResponse)
                .toList();
    }

    public void delete(UUID id) {
        interventionRepository.deleteById(id);
    }

    public InterventionResponseDTO assignTechnician(UUID interventionId, UUID technicianId) {
        Intervention intervention = interventionRepository.findById(interventionId)
                .orElseThrow(() -> new RuntimeException("Intervention not found"));

        Technician technician = technicianRepository.findById(technicianId)
                .orElseThrow(() -> new RuntimeException("Technician not found"));

        intervention.setTechnician(technician);
        return interventionMapper.toResponse(interventionRepository.save(intervention));
    }

    public InterventionResponseDTO startIntervention(UUID interventionId) {
        Intervention intervention = interventionRepository.findById(interventionId)
                .orElseThrow(() -> new RuntimeException("Intervention not found"));

        if (intervention.getStatus() != PLANNED) {
            throw new RuntimeException("Intervention must be PLANNED to start");
        }

        intervention.setStatus(IN_PROGRESS);
        intervention.setStartedAt(LocalDateTime.now());
        return interventionMapper.toResponse(interventionRepository.save(intervention));
    }

    public InterventionResponseDTO completeIntervention(UUID interventionId) {
        Intervention intervention = interventionRepository.findById(interventionId)
                .orElseThrow(() -> new RuntimeException("Intervention not found"));

        if (intervention.getStatus() != IN_PROGRESS) {
            throw new RuntimeException("Intervention must be IN_PROGRESS to complete");
        }

        intervention.setStatus(DONE);
        intervention.setCompletedAt(LocalDateTime.now());
        return interventionMapper.toResponse(interventionRepository.save(intervention));
    }

    public InterventionTimelineDTO getTimeline(UUID id) {
        Intervention intervention = interventionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Intervention not found"));

        Long duration = null;

        if (intervention.getStartedAt() != null && intervention.getCompletedAt() != null) {
            duration = Duration.between(
                    intervention.getStartedAt(),
                    intervention.getCompletedAt()
            ).toMinutes();
        }

        return new InterventionTimelineDTO(
                intervention.getId(),
                intervention.getScheduledAt(),
                intervention.getStartedAt(),
                intervention.getCompletedAt(),
                duration
        );
    }

    public Page<InterventionResponseDTO> search(
            InterventionStatus status,
            LocalDateTime from,
            LocalDateTime to,
            Pageable pageable
    ) {
        return interventionRepository.search(status, from, to, pageable)
                .map(interventionMapper::toResponse);
    }

    public InterventionResponseDTO update(UUID id, InterventionUpdateRequestDTO dto) {

        Intervention intervention = interventionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Intervention not found"));

        // Mise à jour des champs simples
        if (dto.title() != null) intervention.setTitle(dto.title());
        if (dto.description() != null) intervention.setDescription(dto.description());
        if (dto.scheduledAt() != null) intervention.setScheduledAt(dto.scheduledAt());

        // Mise à jour de l'équipement
        if (dto.equipmentId() != null) {
            Equipment equipment = equipmentRepository.findById(dto.equipmentId())
                    .orElseThrow(() -> new RuntimeException("Equipment not found"));
            intervention.setEquipment(equipment);
        }

        // Mise à jour du technicien
        if (dto.technicianId() != null) {
            Technician technician = technicianRepository.findById(dto.technicianId())
                    .orElseThrow(() -> new RuntimeException("Technician not found"));
            intervention.setTechnician(technician);
        }

        intervention = interventionRepository.save(intervention);

        return interventionMapper.toResponse(intervention);
    }
    public void deactivate(UUID id) {
        Intervention intervention = interventionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Intervention not found"));

        intervention.deactivate();
        interventionRepository.save(intervention);
    }

}
