package com.att.tdp.popcorn_palace.controller;

import com.att.tdp.popcorn_palace.dto.MovieDto;
import com.att.tdp.popcorn_palace.service.MovieService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping({"/api/movies"})
public class MovieController {

    private final MovieService movieService;
    
    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<MovieDto>> getAllMoviesWithAllPath() {
        return ResponseEntity.ok(movieService.getAllMovies());
    }
    
    
    @PostMapping
    public ResponseEntity<MovieDto> createMovie(@Valid @RequestBody MovieDto movieDto) {
        return new ResponseEntity<>(movieService.createMovie(movieDto), HttpStatus.CREATED);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<MovieDto> getMovieById(@PathVariable Long id) {
        return ResponseEntity.ok(movieService.getMovieById(id));
    }
    
    @GetMapping
    public ResponseEntity<List<MovieDto>> getAllMovies() {
        return ResponseEntity.ok(movieService.getAllMovies());
    }
    
    @PostMapping("/title/{movieTitle}")
    public ResponseEntity<MovieDto> updateMovieByTitle(@PathVariable String movieTitle, 
                                                     @Valid @RequestBody MovieDto movieDto) {
        return ResponseEntity.ok(movieService.updateMovieByName(movieTitle, movieDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MovieDto> updateMovie(@PathVariable Long id, 
                                               @Valid @RequestBody MovieDto movieDto) {
        return ResponseEntity.ok(movieService.updateMovie(id, movieDto));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMovie(@PathVariable Long id) {
        movieService.deleteMovie(id);
        return ResponseEntity.noContent().build();
    }
    @DeleteMapping("/title/{title}")
    public ResponseEntity<Void> deleteMovie(@PathVariable String title) {
        movieService.deleteMovieByTitle(title);
        return ResponseEntity.noContent().build();
    }
}