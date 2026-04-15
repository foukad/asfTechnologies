package fr.asf.heatops.repository;

import fr.asf.heatops.domain.entity.Intervention;
import fr.asf.heatops.enums.InterventionStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface InterventionRepository extends JpaRepository<Intervention, UUID> {

    List<Intervention> findByClientId(UUID clientId);

    List<Intervention> findByEquipmentId(UUID equipmentId);

    List<Intervention> findByTechnicianId(UUID technicianId);

    @Query("""
    SELECT i FROM Intervention i
    WHERE (:status IS NULL OR i.status = :status)
      AND (:from IS NULL OR i.scheduledAt >= :from)
      AND (:to IS NULL OR i.scheduledAt <= :to)
""")
    Page<Intervention> search(
            @Param("status") InterventionStatus status,
            @Param("from") LocalDateTime from,
            @Param("to") LocalDateTime to,
            Pageable pageable
    );

}
