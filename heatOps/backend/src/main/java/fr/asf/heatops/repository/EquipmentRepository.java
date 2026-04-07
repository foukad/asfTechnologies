package fr.asf.heatops.repository;

import fr.asf.heatops.domain.entity.Intervention;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface InterventionRepository extends JpaRepository<Intervention, UUID> {
}
