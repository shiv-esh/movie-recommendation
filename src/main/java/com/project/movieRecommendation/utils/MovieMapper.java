package com.project.movieRecommendation.utils;

import com.project.movieRecommendation.dto.MovieRequest;
import com.project.movieRecommendation.dto.MovieResponse;
import com.project.movieRecommendation.entity.Movie;
import com.project.movieRecommendation.entity.elastic.MovieDocument;
import org.springframework.stereotype.Component;

@Component
public class MovieMapper {

    public Movie toEntity(MovieRequest request) {
        return Movie.builder()
                .title(request.getTitle())
                .genre(request.getGenre())
                .releaseYear(request.getReleaseYear())
                .rating(request.getRating())
                .cast(request.getCast())
                .build();
    }

    public MovieDocument toDocument(Movie movie) {
        return MovieDocument.builder()
                .id(movie.getId())
                .title(movie.getTitle())
                .genre(movie.getGenre())
                .releaseYear(movie.getReleaseYear())
                .rating(movie.getRating())
                .cast(movie.getCast())
                .build();
    }

    public MovieResponse toResponse(Movie movie) {
        return MovieResponse.builder()
                .id(movie.getId())
                .title(movie.getTitle())
                .genre(movie.getGenre())
                .releaseYear(movie.getReleaseYear())
                .rating(movie.getRating())
                .cast(movie.getCast())
                .build();
    }
}
