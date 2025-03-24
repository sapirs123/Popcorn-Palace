package com.att.tdp.popcorn_palace.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "showtimes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Showtime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "movie_id", nullable = false)
    @NotNull(message = "Movie is required")
    private Long movieId;

    @NotNull(message = "Theater name is required")
    @JoinColumn(name = "theater_name", nullable = false)
    private String theaterName;

    @NotNull(message = "Start time is required")
    private LocalDateTime startTime;

    @NotNull(message = "End time is required")
    private LocalDateTime endTime;

    @Positive(message = "Price must be positive")
    private Double price;
}