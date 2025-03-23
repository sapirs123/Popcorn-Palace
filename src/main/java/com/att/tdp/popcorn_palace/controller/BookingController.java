package com.att.tdp.popcorn_palace.controller;

import com.att.tdp.popcorn_palace.dto.BookingDto;
import com.att.tdp.popcorn_palace.service.BookingService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping({"/api/bookings"})
public class BookingController {

    private final BookingService bookingService;
    
    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }
    
    @PostMapping
    public ResponseEntity<BookingDto> createBooking(@Valid @RequestBody BookingDto bookingDto) {
        return new ResponseEntity<>(bookingService.createBooking(bookingDto), HttpStatus.CREATED);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<BookingDto> getBookingById(@PathVariable Long id) {
        return ResponseEntity.ok(bookingService.getBookingById(id));
    }
    
    @GetMapping
    public ResponseEntity<List<BookingDto>> getAllBookings() {
        return ResponseEntity.ok(bookingService.getAllBookings());
    }
    
    @GetMapping("/showtime/{showtimeId}")
    public ResponseEntity<List<BookingDto>> getBookingsByShowtimeId(@PathVariable Long showtimeId) {
        return ResponseEntity.ok(bookingService.getBookingsByShowtimeId(showtimeId));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<BookingDto> updateBooking(@PathVariable Long id, 
                                                   @Valid @RequestBody BookingDto bookingDto) {
        return ResponseEntity.ok(bookingService.updateBooking(id, bookingDto));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBooking(@PathVariable Long id) {
        bookingService.deleteBooking(id);
        return ResponseEntity.noContent().build();
    }
}