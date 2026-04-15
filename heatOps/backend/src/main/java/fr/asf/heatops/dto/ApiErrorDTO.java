package fr.asf.heatops.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ApiErrorDTO {
    private String message;
    private String error;
    private LocalDateTime timestamp;
}
