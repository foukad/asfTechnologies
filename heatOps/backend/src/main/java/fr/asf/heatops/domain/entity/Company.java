package fr.asf.heatops.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "company")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class Company {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Instant createdAt;
}
