package fr.asf.heatops.repository;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@MappedSuperclass
public abstract class BaseTenantEntity {

    @Column(name = "company_id", nullable = false)
    private String companyId;
}
