package com.project.movieRecommendation.service;

import com.project.movieRecommendation.dto.MovieRequest;
import com.project.movieRecommendation.dto.MovieResponse;
import com.project.movieRecommendation.entity.Movie;
import com.project.movieRecommendation.entity.elastic.MovieDocument;
import com.project.movieRecommendation.repository.MovieRepository;
import com.project.movieRecommendation.utils.MovieMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MovieService {

    private final MovieRepository movieRepository;
    private final MovieSearchService movieSearchService;
    private final MovieMapper movieMapper;

    public MovieResponse createMovie(MovieRequest request)  {

        Movie movie = movieMapper.toEntity(request);
        Movie savedMovie = movieRepository.save(movie);
        MovieDocument document = movieMapper.toDocument(savedMovie);
        movieSearchService.index(document);
        return movieMapper.toResponse(savedMovie);
    }
    public void deleteMovie(String id) {

        if (!movieRepository.existsById(id)) {
            throw new RuntimeException("Movie not found with id: " + id);
        }

        movieRepository.deleteById(id);
        movieSearchService.deleteMovie(id);
    }

    public List<MovieResponse> getAllMovies() {
        List<Movie> movies = movieRepository.findAll();
        return movies.stream().map(movieMapper::toResponse).toList();
    }
}

