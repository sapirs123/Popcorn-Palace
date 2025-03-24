package com.att.tdp.popcorn_palace.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingDto {
    private Long id;

    @NotNull(message = "Showtime ID is required")
    private Long showtimeId;

    @NotNull(message = "User ID is required")
    private Long userId; 

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @Positive(message = "Seat number must be at least 1")
    private Integer numberOfSeats;

    private LocalDateTime bookingTime;
}