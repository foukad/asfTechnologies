package fr.asf.heatops.repository;

import fr.asf.heatops.domain.entity.Equipment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface EquipmentRepository extends JpaRepository<Equipment, UUID> {
    List<Equipment> findByClientId(UUID clientId);

    @Query("""
    SELECT e FROM Equipment e
    WHERE (:search IS NULL 
           OR LOWER(e.model) LIKE LOWER(CONCAT('%', :search, '%'))
           OR LOWER(e.brand) LIKE LOWER(CONCAT('%', :search, '%'))
           OR LOWER(e.model) LIKE LOWER(CONCAT('%', :search, '%')))
      AND (:clientId IS NULL OR e.client.id = :clientId)
      AND (:models IS NULL OR e.model IN :models)
      AND (:brands IS NULL OR e.brand IN :brands)
""")
    Page<Equipment> search(
            @Param("search") String search,
            @Param("clientId") UUID clientId,
            @Param("models") List<String> models,
            @Param("brands") List<String> brands,
            Pageable pageable
    );

}
