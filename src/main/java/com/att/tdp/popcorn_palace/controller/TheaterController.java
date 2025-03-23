package com.att.tdp.popcorn_palace.controller;

import com.att.tdp.popcorn_palace.dto.TheaterDto;
import com.att.tdp.popcorn_palace.service.TheaterService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping({"/api/theaters"})
public class TheaterController {

    private final TheaterService theaterService;
    
    public TheaterController(TheaterService theaterService) {
        this.theaterService = theaterService;
    }
    
    @PostMapping
    public ResponseEntity<TheaterDto> createTheater(@Valid @RequestBody TheaterDto theaterDto) {
        return new ResponseEntity<>(theaterService.createTheater(theaterDto), HttpStatus.CREATED);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<TheaterDto> getTheaterById(@PathVariable Long id) {
        return ResponseEntity.ok(theaterService.getTheaterById(id));
    }
    
    @GetMapping
    public ResponseEntity<List<TheaterDto>> getAllTheaters() {
        return ResponseEntity.ok(theaterService.getAllTheaters());
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<TheaterDto> updateTheater(@PathVariable Long id, 
                                                   @Valid @RequestBody TheaterDto theaterDto) {
        return ResponseEntity.ok(theaterService.updateTheater(id, theaterDto));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTheater(@PathVariable Long id) {
        theaterService.deleteTheater(id);
        return ResponseEntity.noContent().build();
    }
}