package com.att.tdp.popcorn_palace.service.impl;

import com.att.tdp.popcorn_palace.dto.ShowtimeDto;
import com.att.tdp.popcorn_palace.entity.Movie;
import com.att.tdp.popcorn_palace.entity.Showtime;
import com.att.tdp.popcorn_palace.entity.Theater;
import com.att.tdp.popcorn_palace.exception.ResourceNotFoundException;
import com.att.tdp.popcorn_palace.exception.ShowtimeConflictException;
import com.att.tdp.popcorn_palace.repository.MovieRepository;
import com.att.tdp.popcorn_palace.repository.ShowtimeRepository;
import com.att.tdp.popcorn_palace.repository.TheaterRepository;
import com.att.tdp.popcorn_palace.service.ShowtimeService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ShowtimeServiceImpl implements ShowtimeService {
    
    private final ShowtimeRepository showtimeRepository;
    private final MovieRepository movieRepository;
    private final TheaterRepository theaterRepository;
    
    public ShowtimeServiceImpl(ShowtimeRepository showtimeRepository, 
                              MovieRepository movieRepository,
                              TheaterRepository theaterRepository) {
        this.showtimeRepository = showtimeRepository;
        this.movieRepository = movieRepository;
        this.theaterRepository = theaterRepository;
    }
    
    @Override
    @Transactional
    public ShowtimeDto createShowtime(ShowtimeDto showtimeDto) {
        movieRepository.findById(showtimeDto.getMovieId())
                .orElseThrow(() -> new ResourceNotFoundException("Movie", "id", showtimeDto.getMovieId()));
        
        Theater theater = theaterRepository.findById(showtimeDto.getTheaterId())
                .orElseThrow(() -> new ResourceNotFoundException("Theater", "id", showtimeDto.getTheaterId()));
        
        checkForOverlappingShowtimes(null, theater.getId(), 
                                    showtimeDto.getStartTime(), showtimeDto.getEndTime());
        
        Showtime showtime = new Showtime();
        BeanUtils.copyProperties(showtimeDto, showtime);
        Showtime savedShowtime = showtimeRepository.save(showtime);

        ShowtimeDto savedShowtimeDto = new ShowtimeDto();
        BeanUtils.copyProperties(savedShowtime, savedShowtimeDto);
        return savedShowtimeDto;
    }
    
    @Override
    public ShowtimeDto getShowtimeById(Long id) {
        Showtime showtime = showtimeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Showtime", "id", id));
        
        ShowtimeDto showtimeDto = new ShowtimeDto();
        BeanUtils.copyProperties(showtime, showtimeDto);
        return showtimeDto;
    }
    
    @Override
    public List<ShowtimeDto> getAllShowtimes() {
        return showtimeRepository.findAll().stream()
                .map(showtime -> {
                    ShowtimeDto showtimeDto = new ShowtimeDto();
                    BeanUtils.copyProperties(showtime, showtimeDto);
                    return showtimeDto;
                })
                .collect(Collectors.toList());
    }
    
    @Override
    public List<ShowtimeDto> getShowtimesByMovieId(Long movieId) {
        if (!movieRepository.existsById(movieId)) {
            throw new ResourceNotFoundException("Movie", "id", movieId);
        }
        
        return showtimeRepository.findByMovieId(movieId).stream()
                .map(showtime -> {
                    ShowtimeDto showtimeDto = new ShowtimeDto();
                    BeanUtils.copyProperties(showtime, showtimeDto);
                    showtimeDto.setMovieTitle(showtime.getMovie().getTitle());
                    return showtimeDto;
                })
                .collect(Collectors.toList());
    }
    
    @Override
    public List<ShowtimeDto> getShowtimesByTheaterId(Long theaterId) {
        if (!theaterRepository.existsById(theaterId)) {
            throw new ResourceNotFoundException("Theater", "id", theaterId);
        }
        
        return showtimeRepository.findByTheaterId(theaterId).stream()
                .map(showtime -> {
                    ShowtimeDto showtimeDto = new ShowtimeDto();
                    BeanUtils.copyProperties(showtime, showtimeDto);
                    showtimeDto.setTheaterName(showtime.getTheater().getName());
                    return showtimeDto;
                })
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional
    public ShowtimeDto updateShowtime(Long id, ShowtimeDto showtimeDto) {
        Showtime showtime = showtimeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Showtime", "id", id));
        
        Movie movie = movieRepository.findById(showtimeDto.getMovieId())
                .orElseThrow(() -> new ResourceNotFoundException("Movie", "id", showtimeDto.getMovieId()));
        
        Theater theater = theaterRepository.findById(showtimeDto.getTheaterId())
                .orElseThrow(() -> new ResourceNotFoundException("Theater", "id", showtimeDto.getTheaterId()));
        
        checkForOverlappingShowtimes(id, theater.getId(), 
                                    showtimeDto.getStartTime(), showtimeDto.getEndTime());
        
        showtime.setMovie(movie);
        showtime.setTheater(theater);
        showtime.setStartTime(showtimeDto.getStartTime());
        showtime.setEndTime(showtimeDto.getEndTime());
        showtime.setPrice(showtimeDto.getPrice());
        
        Showtime updatedShowtime = showtimeRepository.save(showtime);
        
        ShowtimeDto updatedShowtimeDto = new ShowtimeDto();
        BeanUtils.copyProperties(updatedShowtime, updatedShowtimeDto);
        updatedShowtimeDto.setMovieTitle(updatedShowtime.getMovie().getTitle());
        updatedShowtimeDto.setTheaterName(updatedShowtime.getTheater().getName());
        return updatedShowtimeDto;
    }
    
    @Override
    @Transactional
    public void deleteShowtime(Long id) {
        Showtime showtime = showtimeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Showtime", "id", id));
        
        showtimeRepository.delete(showtime);
    }
    
    private void checkForOverlappingShowtimes(Long currentShowtimeId, Long theaterId, 
                                             LocalDateTime startTime, LocalDateTime endTime) {
        Theater theater = theaterRepository.findById(theaterId).orElseThrow(() -> new ResourceNotFoundException("Theater", "id", theaterId));
        List<Showtime> overlappingShowtimes = showtimeRepository.findOverlappingShowtimes(theater, startTime, endTime);

        // If updating an existing showtime, exclude it from the overlap check
        if (currentShowtimeId != null) {
            overlappingShowtimes = overlappingShowtimes.stream()
                .filter(showtime -> !showtime.getId().equals(currentShowtimeId))
                .collect(Collectors.toList());
        }
        
        if (!overlappingShowtimes.isEmpty()) {
            throw new ShowtimeConflictException("There is already a showtime scheduled in this theater that overlaps with the requested time");
        }
    }
}