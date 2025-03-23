package com.att.tdp.popcorn_palace.repository;

import com.att.tdp.popcorn_palace.entity.Booking;
import com.att.tdp.popcorn_palace.entity.Showtime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByShowtime(Showtime showtime);
    List<Booking> findByUserId(Long userId);

    @Query("SELECT SUM(b.numberOfSeats) FROM Booking b WHERE b.showtime.id = :showtimeId")
    Integer countSeatsByShowtimeId(@Param("showtimeId") Long showtimeId);
}