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
    
    @GetMapping("/{name}")
    public ResponseEntity<TheaterDto> getTheaterById(@PathVariable String name) {
        return ResponseEntity.ok(theaterService.getTheaterByName(name));
    }
    
    @GetMapping
    public ResponseEntity<List<TheaterDto>> getAllTheaters() {
        return ResponseEntity.ok(theaterService.getAllTheaters());
    }
    
    @PutMapping("/{name}")
    public ResponseEntity<TheaterDto> updateTheater(@PathVariable String name, 
                                                   @Valid @RequestBody TheaterDto theaterDto) {
        return ResponseEntity.ok(theaterService.updateTheater(name, theaterDto));
    }
    
    @DeleteMapping("/{name}")
    public ResponseEntity<Void> deleteTheater(@PathVariable String name) {
        theaterService.deleteTheater(name);
        return ResponseEntity.noContent().build();
    }
}