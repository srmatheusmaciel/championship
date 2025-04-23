package com.matheusmaciel.championship.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class MatchGenerationDTO {

    @Schema(example = "2023-01-01T00:00:00", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "First round date is required")
    private LocalDateTime firstRoundDate;

    @Schema(example = "[\"2025-05-15\", \"2025-06-12\"]")
    private List<LocalDate> invalidDates;
}
