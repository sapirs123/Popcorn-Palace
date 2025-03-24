package com.att.tdp.popcorn_palace.service;

import com.att.tdp.popcorn_palace.dto.ShowtimeDto;
import java.util.List;

public interface ShowtimeService {
    ShowtimeDto createShowtime(ShowtimeDto showtimeDto);
    ShowtimeDto updateShowtime(Long id, ShowtimeDto showtimeDto);
    void deleteShowtime(Long id);
    
    ShowtimeDto getShowtimeById(Long id);
    List<ShowtimeDto> getAllShowtimes();
    List<ShowtimeDto> getShowtimesByMovieId(Long movieId);
    public List<ShowtimeDto> getShowtimesByTheaterName(String theaterName);
    
}