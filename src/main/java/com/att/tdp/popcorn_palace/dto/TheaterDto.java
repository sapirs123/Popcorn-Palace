package com.att.tdp.popcorn_palace.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TheaterDto {
    private Long id;

    @NotBlank(message = "Name is required")
    private String name;

    @Positive(message = "Capacity must be positive")
    private Integer capacity;

    @NotBlank(message = "Location is required")
    private String location;
}