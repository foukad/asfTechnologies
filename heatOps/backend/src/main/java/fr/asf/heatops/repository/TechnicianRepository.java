package fr.asf.heatops.repository;

import fr.asf.heatops.domain.entity.Technician;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface TechnicianRepository extends JpaRepository<Technician, UUID> {
    @Query("""
    SELECT t FROM Technician t
    WHERE (:search IS NULL 
           OR LOWER(t.firstName) LIKE LOWER(CONCAT('%', :search, '%'))
           OR LOWER(t.lastName) LIKE LOWER(CONCAT('%', :search, '%'))
           OR LOWER(t.phone) LIKE LOWER(CONCAT('%', :search, '%')))
""")
    Page<Technician> search(
            @Param("search") String search,
            Pageable pageable
    );

}
