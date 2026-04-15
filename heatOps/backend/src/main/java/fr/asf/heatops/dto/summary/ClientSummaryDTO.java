package fr.asf.heatops.dto.summary;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class ClientSummaryDTO {
    private UUID id;
    private String firstName;
    private String lastName;
}

