package fr.asf.heatops.service;

import fr.asf.heatops.domain.entity.Client;
import fr.asf.heatops.domain.entity.User;
import fr.asf.heatops.dto.client.ClientRequestDTO;
import fr.asf.heatops.dto.client.ClientResponseDTO;
import fr.asf.heatops.mapper.ClientMapper;
import fr.asf.heatops.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ClientService {

    private final ClientRepository clientRepository;
    private final ClientMapper clientMapper;

    public ClientResponseDTO create(ClientRequestDTO dto) {
        Client client = clientMapper.toEntity(dto);
        client = clientRepository.save(client);
        return clientMapper.toResponse(client);
    }

    public List<ClientResponseDTO> findAll() {
        return clientRepository.findAll()
                .stream()
                .map(clientMapper::toResponse)
                .toList();
    }

    public ClientResponseDTO findById(UUID id) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Client not found"));
        return clientMapper.toResponse(client);
    }

    public void delete(UUID id) {
        clientRepository.deleteById(id);
    }

    public Page<ClientResponseDTO> findAll(Pageable pageable) {
        return clientRepository.findAllByCompanyId(getCompanyId(), pageable)
                .map(clientMapper::toResponse);
    }

    public Page<ClientResponseDTO> search(String search, Pageable pageable) {
        return clientRepository.search( search, pageable)
                .map(clientMapper::toResponse);
    }

    private static UUID getCompanyId() {
        //Todo: à modifier
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return user.getCompany().getId();
    }

    public void deactivate(UUID id) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Client not found"));

        client.deactivate(); // active = false
        clientRepository.save(client);
    }


}
