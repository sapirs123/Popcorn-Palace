package com.att.tdp.popcorn_palace.service.impl;

import com.att.tdp.popcorn_palace.dto.BookingDto;
import com.att.tdp.popcorn_palace.entity.Booking;
import com.att.tdp.popcorn_palace.entity.Showtime;
import com.att.tdp.popcorn_palace.exception.SeatNotAvailableException;
import com.att.tdp.popcorn_palace.exception.ResourceNotFoundException;
import com.att.tdp.popcorn_palace.repository.BookingRepository;
import com.att.tdp.popcorn_palace.repository.ShowtimeRepository;
import com.att.tdp.popcorn_palace.service.BookingService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final ShowtimeRepository showtimeRepository;
    

    public BookingServiceImpl(BookingRepository bookingRepository,
                             ShowtimeRepository showtimeRepository) {
        this.bookingRepository = bookingRepository;
        this.showtimeRepository = showtimeRepository;
    }

    @Override
    @Transactional
    public BookingDto createBooking(BookingDto bookingDto) {
        Showtime showtime = showtimeRepository.findById(bookingDto.getShowtimeId())
                .orElseThrow(() -> new ResourceNotFoundException("Showtime", "id", bookingDto.getShowtimeId()));
        
        int currentlyBookedSeats = bookingRepository.countSeatsByShowtimeId(showtime.getId());
        int requestedSeats = bookingDto.getNumberOfSeats();
        
        if (currentlyBookedSeats + requestedSeats > showtime.getTheater().getCapacity()) {
            int availableSeats = showtime.getTheater().getCapacity() - currentlyBookedSeats;
            throw new SeatNotAvailableException("Not enough seats available. Requested: " + requestedSeats + ", Available: " + availableSeats);
        }
        
        Booking booking = new Booking();
        BeanUtils.copyProperties(bookingDto, booking);
        booking.setShowtime(showtime);
        booking.setBookingTime(LocalDateTime.now());
        Booking savedBooking = bookingRepository.save(booking);

        BookingDto savedBookingDto = new BookingDto();
        BeanUtils.copyProperties(savedBooking, savedBookingDto);
        return savedBookingDto;
    }

    
    @Override
    @Transactional
    public BookingDto updateBooking(Long id, BookingDto bookingDto) {
        Showtime showtime = showtimeRepository.findById(bookingDto.getShowtimeId())
            .orElseThrow(() -> new ResourceNotFoundException("Showtime", "id", bookingDto.getShowtimeId()));
        
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking", "id", id));

        
        // if the seat number is bigger, check if there are enough seats available
        int updatedSeats = bookingDto.getNumberOfSeats();
        if (!booking.getShowtime().getId().equals(showtime.getId()) || booking.getNumberOfSeats() < updatedSeats) {
            int currentlyBookedSeats = bookingRepository.countSeatsByShowtimeId(showtime.getId());
            int additionalSeats = updatedSeats - booking.getNumberOfSeats();
            
            if (currentlyBookedSeats + additionalSeats > showtime.getTheater().getCapacity()) {
                int availableSeats = showtime.getTheater().getCapacity() - currentlyBookedSeats;
                throw new SeatNotAvailableException("Not enough seats available. Requested: " + updatedSeats + ", Available: " + availableSeats);
            }
        }
    
        booking.setShowtime(showtime);
        booking.setUserId(bookingDto.getUserId());
        booking.setEmail(bookingDto.getEmail());
        booking.setNumberOfSeats(updatedSeats);
        booking.setBookingTime(bookingDto.getBookingTime());

        Booking updatedBooking = bookingRepository.save(booking);

        BookingDto updatedBookingDto = new BookingDto();
        BeanUtils.copyProperties(updatedBooking, updatedBookingDto);
        return updatedBookingDto;
    }

    @Override
    @Transactional
    public void deleteBooking(Long id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking", "id", id));
        bookingRepository.delete(booking);
    }

    @Override
    public BookingDto getBookingById(Long id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking", "id", id));

        BookingDto bookingDto = new BookingDto();
        BeanUtils.copyProperties(booking, bookingDto);
        return bookingDto;
    }

    @Override
    public List<BookingDto> getAllBookings() {
        return bookingRepository.findAll().stream()
                .map(booking -> {
                    BookingDto bookingDto = new BookingDto();
                    BeanUtils.copyProperties(booking, bookingDto);
                    return bookingDto;
                })
                .collect(Collectors.toList());
    }


    @Override
    public List<BookingDto> getBookingsByUserId(Long userId) {
        return bookingRepository.findByUserId(userId).stream()
                .map(booking -> {
                    BookingDto bookingDto = new BookingDto();
                    BeanUtils.copyProperties(booking, bookingDto);
                    return bookingDto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<BookingDto> getBookingsByShowtimeId(Long showtimeId) {
        Showtime showtime = showtimeRepository.findById(showtimeId)
            .orElseThrow(() -> new ResourceNotFoundException("Showtime", "id", showtimeId));
        
        return bookingRepository.findByShowtime(showtime).stream()
                .map(booking -> {
                    BookingDto bookingDto = new BookingDto();
                    BeanUtils.copyProperties(booking, bookingDto);
                    return bookingDto;
                })
                .collect(Collectors.toList());
    }
}
