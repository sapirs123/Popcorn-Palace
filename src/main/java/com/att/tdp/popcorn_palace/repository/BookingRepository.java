package com.att.tdp.popcorn_palace.repository;

import com.att.tdp.popcorn_palace.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByShowtimeId(Long showtimeId);
    List<Booking> findByUserId(Long userId);

    @Query("SELECT COALESCE(SUM(b.numberOfSeats), 0) FROM Booking b WHERE b.showtimeId = :showtimeId")
    Integer countSeatsByShowtimeId(@Param("showtimeId") Long showtimeId);
}