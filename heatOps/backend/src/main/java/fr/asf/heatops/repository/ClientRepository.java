package fr.asf.heatops.repository;

import fr.asf.heatops.domain.entity.Client;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface ClientRepository extends JpaRepository<Client, UUID> {
    Page<Client> findAllByCompanyId(UUID companyId, Pageable pageable);

    @Query("""
    SELECT c FROM Client c
    WHERE (:name IS NULL OR LOWER(c.firstName) LIKE LOWER(CONCAT('%', :name, '%'))
           OR LOWER(c.lastName) LIKE LOWER(CONCAT('%', :name, '%')))
      AND (:email IS NULL OR LOWER(c.email) LIKE LOWER(CONCAT('%', :email, '%')))
""")
    Page<Client> search(
            @Param("search") String search,
            Pageable pageable
    );
}
