package com.att.tdp.popcorn_palace.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "bookings")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "showtime_id", nullable = false)
    @NotNull(message = "Showtime is required")
    private Showtime showtime;

    @NotNull(message = "User ID is required")
    private Long userId; 

    @NotNull(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @Positive(message = "Seat number must be at least 1")
    private Integer numberOfSeats;

    @NotNull(message = "Booking time is required")
    private LocalDateTime bookingTime;
}