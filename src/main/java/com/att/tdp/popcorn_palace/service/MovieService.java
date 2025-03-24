package com.att.tdp.popcorn_palace.service;

import com.att.tdp.popcorn_palace.dto.MovieDto;

import java.util.List;

public interface MovieService {
    MovieDto createMovie(MovieDto movieDto);
    MovieDto updateMovie(Long id, MovieDto movieDto);
    MovieDto updateMovieByName(String name, MovieDto movieDto);
    void deleteMovie(Long id);
    public void deleteMovieByTitle(String title);

    MovieDto getMovieById(Long id);
    List<MovieDto> getAllMovies();
}