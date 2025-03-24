package com.att.tdp.popcorn_palace.dto;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShowtimeDto {
    private Long id;

    @NotNull(message = "Movie ID is required")
    private Long movieId;

    @NotNull(message = "Theater ID is required")
    private String theaterName;

    @NotNull(message = "Start time is required")
    private LocalDateTime startTime;

    @NotNull(message = "End time is required")
    private LocalDateTime endTime;

    @Positive(message = "Price must be positive")
    private Double price;    
}