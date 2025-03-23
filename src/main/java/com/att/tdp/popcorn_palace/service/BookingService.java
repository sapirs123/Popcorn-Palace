package com.att.tdp.popcorn_palace.service;

import com.att.tdp.popcorn_palace.dto.BookingDto;
import java.util.List;

public interface BookingService {
    BookingDto createBooking(BookingDto bookingDto);
    BookingDto updateBooking(Long id, BookingDto bookingDto);
    void deleteBooking(Long id);

    BookingDto getBookingById(Long id);
    List<BookingDto> getBookingsByShowtimeId(Long showtimeId);
    List<BookingDto> getBookingsByUserId(Long userId);
    List<BookingDto> getAllBookings();
}