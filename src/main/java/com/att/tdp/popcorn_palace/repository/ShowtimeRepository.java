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
    boolean existsByTheaterId(Long theaterId);
    List<Showtime> findByMovieId(Long movieId);
    List<Showtime> findByTheaterId(Long theaterId);
    
    // Find all showtimes for a theater that overlap with the time range provided
    @Query("SELECT s FROM Showtime s WHERE s.theater = :theater AND " +
           "((s.startTime <= :endTime AND s.endTime >= :startTime) OR " +
           "(s.startTime <= :startTime AND s.endTime >= :startTime) OR " +
           "(s.startTime <= :endTime AND s.endTime >= :endTime))")
    List<Showtime> findOverlappingShowtimes(Theater theater, 
                                            LocalDateTime startTime, 
                                            LocalDateTime endTime);
}