package com.att.tdp.popcorn_palace.service.impl;

import com.att.tdp.popcorn_palace.dto.TheaterDto;
import com.att.tdp.popcorn_palace.entity.Theater;
import com.att.tdp.popcorn_palace.exception.ResourceNotFoundException;
import com.att.tdp.popcorn_palace.repository.ShowtimeRepository;
import com.att.tdp.popcorn_palace.repository.TheaterRepository;
import com.att.tdp.popcorn_palace.service.TheaterService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TheaterServiceImpl implements TheaterService {
    
    private final TheaterRepository theaterRepository;
    private final ShowtimeRepository showtimeRepository;

    public TheaterServiceImpl(TheaterRepository theaterRepository, 
                             ShowtimeRepository showtimeRepository) {
        this.theaterRepository = theaterRepository;
        this.showtimeRepository = showtimeRepository;
    }
    
    @Override
    @Transactional
    public TheaterDto createTheater(TheaterDto theaterDto) {
        Theater theater = new Theater();
        BeanUtils.copyProperties(theaterDto, theater);
        
        Theater savedTheater = theaterRepository.save(theater);
        
        TheaterDto savedTheaterDto = new TheaterDto();
        BeanUtils.copyProperties(savedTheater, savedTheaterDto);
        return savedTheaterDto;
    }
    
    @Override
    public TheaterDto getTheaterById(Long id) {
        Theater theater = theaterRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Theater", "id", id));
        
        TheaterDto theaterDto = new TheaterDto();
        BeanUtils.copyProperties(theater, theaterDto);
        return theaterDto;
    }
    
    @Override
    public List<TheaterDto> getAllTheaters() {
        return theaterRepository.findAll().stream()
                .map(theater -> {
                    TheaterDto theaterDto = new TheaterDto();
                    BeanUtils.copyProperties(theater, theaterDto);
                    return theaterDto;
                })
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional
    public TheaterDto updateTheater(Long id, TheaterDto theaterDto) {
        Theater theater = theaterRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Theater", "id", id));
        
        theater.setName(theaterDto.getName());
        theater.setLocation(theaterDto.getLocation());
        theater.setCapacity(theaterDto.getCapacity());
        
        Theater updatedTheater = theaterRepository.save(theater);
        
        TheaterDto updatedTheaterDto = new TheaterDto();
        BeanUtils.copyProperties(updatedTheater, updatedTheaterDto);
        return updatedTheaterDto;
    }
    
    @Override
    @Transactional
    public void deleteTheater(Long id) {
        Theater theater = theaterRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Theater", "id", id));
        
        // Only delete if there are no scheduled showtimes for this theater
        if (showtimeRepository.existsByTheaterId(id)) {
            throw new IllegalStateException("Cannot delete theater with scheduled showtimes");
        }
        
        theaterRepository.delete(theater);
    }
}