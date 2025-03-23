package com.att.tdp.popcorn_palace.service;

import com.att.tdp.popcorn_palace.dto.TheaterDto;
import java.util.List;

public interface TheaterService {
    TheaterDto createTheater(TheaterDto theaterDto);
    TheaterDto updateTheater(Long id, TheaterDto theaterDto);
    void deleteTheater(Long id);

    TheaterDto getTheaterById(Long id);
    List<TheaterDto> getAllTheaters();
}