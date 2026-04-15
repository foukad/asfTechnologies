package fr.asf.heatops.service;

import fr.asf.heatops.domain.entity.Client;
import fr.asf.heatops.domain.entity.Equipment;
import fr.asf.heatops.dto.equipment.EquipmentRequestDTO;
import fr.asf.heatops.dto.equipment.EquipmentResponseDTO;
import fr.asf.heatops.mapper.EquipmentMapper;
import fr.asf.heatops.repository.ClientRepository;
import fr.asf.heatops.repository.EquipmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EquipmentService {

    private final EquipmentRepository equipmentRepository;
    private final ClientRepository clientRepository;
    private final EquipmentMapper equipmentMapper;

    public EquipmentResponseDTO create(UUID clientId, EquipmentRequestDTO dto) {
        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new RuntimeException("Client not found"));

        Equipment equipment = equipmentMapper.toEntity(dto);
        equipment.setClient(client);

        equipment = equipmentRepository.save(equipment);
        return equipmentMapper.toResponse(equipment);
    }

    public List<EquipmentResponseDTO> findAll() {
        return equipmentRepository.findAll()
                .stream()
                .map(equipmentMapper::toResponse)
                .toList();
    }

    public EquipmentResponseDTO findById(UUID id) {
        Equipment equipment = equipmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Equipment not found"));
        return equipmentMapper.toResponse(equipment);
    }

    public List<EquipmentResponseDTO> findByClient(UUID clientId) {
        return equipmentRepository.findByClientId(clientId)
                .stream()
                .map(equipmentMapper::toResponse)
                .toList();
    }

    public void delete(UUID id) {
        equipmentRepository.deleteById(id);
    }

    public Page<EquipmentResponseDTO> search(
            String search,
            UUID clientId,
            List<String> models,
            List<String> brands,
            Pageable pageable
    ) {
        return equipmentRepository.search(search, clientId, models, brands, pageable)
                .map(equipmentMapper::toResponse);
    }

    public void deactivate(UUID id) {
        Equipment equipment = equipmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Equipment not found"));
        equipment.deactivate();
        equipmentRepository.save(equipment);
    }
}
