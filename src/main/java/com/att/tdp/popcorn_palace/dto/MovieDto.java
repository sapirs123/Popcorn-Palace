package com.att.tdp.popcorn_palace.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MovieDto {
    private Long id;

    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Genre is required")
    private String genre;

    @Min(value = 1, message = "Duration must be at least 1 minute")
    private Integer duration;

    @Min(value = 0, message = "Rating must be at least 0")
    @Max(value = 10, message = "Rating cannot be more than 10")
    private Double rating;

    @Min(value = 1900, message = "Release year must be at least 1900")
    @Max(value = 2025, message = "Release year cannot be later than 2025")
    private Integer releaseYear;
}