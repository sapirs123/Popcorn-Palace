package com.att.tdp.popcorn_palace.service.impl;

import com.att.tdp.popcorn_palace.dto.BookingDto;
import com.att.tdp.popcorn_palace.entity.Booking;
import com.att.tdp.popcorn_palace.entity.Showtime;
import com.att.tdp.popcorn_palace.entity.Theater;
import com.att.tdp.popcorn_palace.exception.SeatNotAvailableException;
import com.att.tdp.popcorn_palace.exception.ResourceNotFoundException;
import com.att.tdp.popcorn_palace.repository.BookingRepository;
import com.att.tdp.popcorn_palace.repository.ShowtimeRepository;
import com.att.tdp.popcorn_palace.repository.TheaterRepository;
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
    private final TheaterRepository theaterRepository;
    

    public BookingServiceImpl(BookingRepository bookingRepository,
                             ShowtimeRepository showtimeRepository, TheaterRepository theaterRepository) {
        this.bookingRepository = bookingRepository;
        this.showtimeRepository = showtimeRepository;
        this.theaterRepository = theaterRepository;
    }

    @Override
    @Transactional
    public BookingDto createBooking(BookingDto bookingDto) {
        Showtime showtime = showtimeRepository.findById(bookingDto.getShowtimeId())
                .orElseThrow(() -> new ResourceNotFoundException("Showtime", "id", bookingDto.getShowtimeId()));

        Theater theater = theaterRepository.findByName(showtime.getTheaterName())
                .orElseThrow(() -> new ResourceNotFoundException("Theater", "id", showtime.getTheaterName()));
        
        
        int currentlyBookedSeats = bookingRepository.countSeatsByShowtimeId(showtime.getId());
        int requestedSeats = bookingDto.getNumberOfSeats();
        
        if (currentlyBookedSeats + requestedSeats > theater.getCapacity()) {
            int availableSeats = theater.getCapacity() - currentlyBookedSeats;
            throw new SeatNotAvailableException("Not enough seats available. Requested: " + requestedSeats + ", Available: " + availableSeats);
        }
        
        Booking booking = new Booking();
        BeanUtils.copyProperties(bookingDto, booking);
        booking.setShowtimeId(showtime.getId());
        booking.setBookingTime(LocalDateTime.now());
        Booking savedBooking = bookingRepository.save(booking);

        BookingDto savedBookingDto = new BookingDto();
        BeanUtils.copyProperties(savedBooking, savedBookingDto);
        savedBookingDto.setShowtimeId(savedBooking.getShowtimeId());
        return savedBookingDto;
    }

    
    @Override
    @Transactional
    public BookingDto updateBooking(Long id, BookingDto bookingDto) {
        Showtime showtime = showtimeRepository.findById(bookingDto.getShowtimeId())
            .orElseThrow(() -> new ResourceNotFoundException("Showtime", "id", bookingDto.getShowtimeId()));
        
        Theater theater = theaterRepository.findByName(showtime.getTheaterName())
            .orElseThrow(() -> new ResourceNotFoundException("Theater", "id", showtime.getTheaterName()));
    
    
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking", "id", id));

        // check if the booking time is being updated
        if (bookingDto.getBookingTime() != null && !bookingDto.getBookingTime().equals(booking.getBookingTime())) {
            throw new IllegalArgumentException("Booking time cannot be updated.");
        }
        
        // if the seat number is bigger, check if there are enough seats available
        int updatedSeats = bookingDto.getNumberOfSeats();
        if (!booking.getShowtimeId().equals(showtime.getId()) || booking.getNumberOfSeats() < updatedSeats) {
            int currentlyBookedSeats = bookingRepository.countSeatsByShowtimeId(showtime.getId());
            int additionalSeats = updatedSeats - booking.getNumberOfSeats();
            
            if (currentlyBookedSeats + additionalSeats > theater.getCapacity()) {
                int availableSeats = theater.getCapacity() - currentlyBookedSeats;
                throw new SeatNotAvailableException("Not enough seats available. Requested: " + updatedSeats + ", Available: " + availableSeats);
            }
        }
    
        booking.setShowtimeId(showtime.getId());
        booking.setUserId(bookingDto.getUserId());
        booking.setEmail(bookingDto.getEmail());
        booking.setNumberOfSeats(updatedSeats);

        Booking updatedBooking = bookingRepository.save(booking);

        BookingDto updatedBookingDto = new BookingDto();
        BeanUtils.copyProperties(updatedBooking, updatedBookingDto);
        updatedBookingDto.setShowtimeId(updatedBooking.getShowtimeId());
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
        bookingDto.setShowtimeId(booking.getShowtimeId());
        return bookingDto;
    }

    @Override
    public List<BookingDto> getAllBookings() {
        return bookingRepository.findAll().stream()
                .map(booking -> {
                    BookingDto bookingDto = new BookingDto();
                    BeanUtils.copyProperties(booking, bookingDto);
                    bookingDto.setShowtimeId(booking.getShowtimeId());
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
                    bookingDto.setShowtimeId(booking.getShowtimeId());
                    return bookingDto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<BookingDto> getBookingsByShowtimeId(Long showtimeId) {
        Showtime showtime = showtimeRepository.findById(showtimeId)
            .orElseThrow(() -> new ResourceNotFoundException("Showtime", "id", showtimeId));
        
        return bookingRepository.findByShowtimeId(showtimeId).stream()
                .map(booking -> {
                    BookingDto bookingDto = new BookingDto();
                    BeanUtils.copyProperties(booking, bookingDto);
                    bookingDto.setShowtimeId(booking.getShowtimeId());
                    return bookingDto;
                })
                .collect(Collectors.toList());
    }
}
