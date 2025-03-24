package com.att.tdp.popcorn_palace.service.impl;

import com.att.tdp.popcorn_palace.dto.MovieDto;
import com.att.tdp.popcorn_palace.entity.Movie;
import com.att.tdp.popcorn_palace.exception.ResourceNotFoundException;
import com.att.tdp.popcorn_palace.repository.MovieRepository;
import com.att.tdp.popcorn_palace.service.MovieService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MovieServiceImpl implements MovieService {
    
    private final MovieRepository movieRepository;
    
    public MovieServiceImpl(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }
    
    @Override
    @Transactional
    public MovieDto createMovie(MovieDto movieDto) {
        if (movieRepository.findByTitle(movieDto.getTitle()).isPresent()) {
            throw new IllegalArgumentException("Movie with title '" + movieDto.getTitle() + "' already exists");
        }
        
        Movie movie = new Movie();
        BeanUtils.copyProperties(movieDto, movie);
        Movie savedMovie = movieRepository.save(movie);
        
        MovieDto savedMovieDto = new MovieDto();
        BeanUtils.copyProperties(savedMovie, savedMovieDto);
        return savedMovieDto;
    }
    
    @Override
    public MovieDto getMovieById(Long id) {
        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Movie", "id", id));
        
        MovieDto movieDto = new MovieDto();
        BeanUtils.copyProperties(movie, movieDto);
        return movieDto;
    }
    
    @Override
    public List<MovieDto> getAllMovies() {
        return movieRepository.findAll().stream()
                .map(movie -> {
                    MovieDto movieDto = new MovieDto();
                    BeanUtils.copyProperties(movie, movieDto);
                    return movieDto;
                })
                .collect(Collectors.toList());
    }


    @Override
    @Transactional
    public MovieDto updateMovieByName(String name, MovieDto movieDto){
        Movie movie = movieRepository.findByTitle(name)
                .orElseThrow(() -> new ResourceNotFoundException("Movie", "name", name));
        
        movie.setGenre(movieDto.getGenre());
        movie.setDuration(movieDto.getDuration());
        movie.setRating(movieDto.getRating());
        movie.setReleaseYear(movieDto.getReleaseYear());
        
        Movie updatedMovie = movieRepository.save(movie);
        
        MovieDto updatedMovieDto = new MovieDto();
        BeanUtils.copyProperties(updatedMovie, updatedMovieDto);
        return updatedMovieDto;
    }
    
    @Override
    @Transactional
    public MovieDto updateMovie(Long id, MovieDto movieDto) {
        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Movie", "id", id));
        
        movie.setTitle(movieDto.getTitle());
        movie.setGenre(movieDto.getGenre());
        movie.setDuration(movieDto.getDuration());
        movie.setRating(movieDto.getRating());
        movie.setReleaseYear(movieDto.getReleaseYear());
        
        Movie updatedMovie = movieRepository.save(movie);
        
        MovieDto updatedMovieDto = new MovieDto();
        BeanUtils.copyProperties(updatedMovie, updatedMovieDto);
        return updatedMovieDto;
    }
    
    @Override
    @Transactional
    public void deleteMovie(Long id) {
        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Movie", "id", id));
        movieRepository.delete(movie);
    }

    @Override
    @Transactional
    public void deleteMovieByTitle(String title) {
        Movie movie = movieRepository.findByTitle(title)
                .orElseThrow(() -> new ResourceNotFoundException("Movie", "title", title));
        movieRepository.delete(movie);
    }
}