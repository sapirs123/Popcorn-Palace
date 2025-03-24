package com.att.tdp.popcorn_palace.controller;

import com.att.tdp.popcorn_palace.dto.ShowtimeDto;
import com.att.tdp.popcorn_palace.service.ShowtimeService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping({"/api/showtimes"})
public class ShowtimeController {

    private final ShowtimeService showtimeService;
    
    public ShowtimeController(ShowtimeService showtimeService) {
        this.showtimeService = showtimeService;
    }
    
    @PostMapping
    public ResponseEntity<ShowtimeDto> createShowtime(@Valid @RequestBody ShowtimeDto showtimeDto) {
        return new ResponseEntity<>(showtimeService.createShowtime(showtimeDto), HttpStatus.CREATED);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ShowtimeDto> getShowtimeById(@PathVariable Long id) {
        return ResponseEntity.ok(showtimeService.getShowtimeById(id));
    }
    
    @GetMapping
    public ResponseEntity<List<ShowtimeDto>> getAllShowtimes() {
        return ResponseEntity.ok(showtimeService.getAllShowtimes());
    }
    
    @GetMapping("/movie/{movieId}")
    public ResponseEntity<List<ShowtimeDto>> getShowtimesByMovieId(@PathVariable Long movieId) {
        return ResponseEntity.ok(showtimeService.getShowtimesByMovieId(movieId));
    }
    
    @GetMapping("/theater/{theaterName}")
    public ResponseEntity<List<ShowtimeDto>> getShowtimesByTheaterName(@PathVariable String theaterName) {
        return ResponseEntity.ok(showtimeService.getShowtimesByTheaterName(theaterName));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<ShowtimeDto> updateShowtime(@PathVariable Long id, 
                                                     @Valid @RequestBody ShowtimeDto showtimeDto) {
        return ResponseEntity.ok(showtimeService.updateShowtime(id, showtimeDto));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteShowtime(@PathVariable Long id) {
        showtimeService.deleteShowtime(id);
        return ResponseEntity.noContent().build();
    }
}