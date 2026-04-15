package fr.asf.heatops.domain.entity;

import fr.asf.heatops.tenant.BaseTenantEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "technician")
public class Technician extends BaseTenantEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String firstName;
    private String lastName;
    private String phone;
    private String email;

    @OneToMany(mappedBy = "technician")
    private List<Intervention> interventions = new ArrayList<>();
}
