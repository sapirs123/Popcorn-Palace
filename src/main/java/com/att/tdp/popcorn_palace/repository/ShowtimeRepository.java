package com.att.tdp.popcorn_palace.repository;

import com.att.tdp.popcorn_palace.entity.Showtime;
import com.att.tdp.popcorn_palace.entity.Theater;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ShowtimeRepository extends JpaRepository<Showtime, Long> {
    boolean existsByTheaterName(String theaterName);
    List<Showtime> findByMovieId(Long movieId);
    List<Showtime> findByTheaterName(String theaterName);
    
    // Find all showtimes for a theater that overlap with the time range provided
    @Query("SELECT s FROM Showtime s WHERE s.theaterName = :theaterName AND " +
           "((s.startTime <= :endTime AND s.endTime >= :startTime) OR " +
           "(s.startTime <= :startTime AND s.endTime >= :startTime) OR " +
           "(s.startTime <= :endTime AND s.endTime >= :endTime))")
    List<Showtime> findOverlappingShowtimes(String theaterName, 
                                            LocalDateTime startTime, 
                                            LocalDateTime endTime);
}