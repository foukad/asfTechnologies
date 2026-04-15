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
@Table(name = "equipment")
public class Equipment extends BaseTenantEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String name;            // ex: Chaudière Saunier Duval
    private String brand;           // ex: Saunier Duval
    private String model;           // ex: ThemaPlus Condens
    private String serialNumber;    // numéro de série
    private Integer year;           // année d'installation

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;

    @OneToMany(mappedBy = "equipment")
    private List<Intervention> interventions = new ArrayList<>();
}
