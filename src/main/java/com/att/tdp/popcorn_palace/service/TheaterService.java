package com.att.tdp.popcorn_palace.service;

import com.att.tdp.popcorn_palace.dto.TheaterDto;
import java.util.List;

public interface TheaterService {
    TheaterDto createTheater(TheaterDto theaterDto);
    TheaterDto updateTheater(String theaterName, TheaterDto theaterDto);
    void deleteTheater(String theaterName);

    TheaterDto getTheaterByName(String theaterNamed);
    List<TheaterDto> getAllTheaters();
}