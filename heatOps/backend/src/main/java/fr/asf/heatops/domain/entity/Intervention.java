package fr.asf.heatops.domain.entity;

import fr.asf.heatops.enums.InterventionStatus;
import fr.asf.heatops.tenant.BaseTenantEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "intervention")
public class Intervention extends BaseTenantEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String title;
    private String description;

    private LocalDateTime scheduledAt;

    private String address;
    private String city;
    private String postalCode;

    private String technicianName;

    @Enumerated(EnumType.STRING)
    private InterventionStatus status = InterventionStatus.PLANNED;

    @Column(name = "started_at")
    private LocalDateTime startedAt;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;

    @ManyToOne
    @JoinColumn(name = "equipment_id")
    private Equipment equipment;

    @ManyToOne
    @JoinColumn(name = "technician_id")
    private Technician technician;

}
