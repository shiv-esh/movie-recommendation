package com.project.movieRecommendation.service;

import com.project.movieRecommendation.dto.MovieRequest;
import com.project.movieRecommendation.dto.MovieResponse;
import com.project.movieRecommendation.entity.Movie;
import com.project.movieRecommendation.entity.elastic.MovieDocument;
import com.project.movieRecommendation.repository.MovieRepository;
import com.project.movieRecommendation.repository.MovieSearchRepository;
import com.project.movieRecommendation.utils.MovieMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Collections;
import java.util.List;


@Service
@RequiredArgsConstructor
@Slf4j
public class MovieSearchService {

    private final MovieSearchRepository searchRepository;
    private final MovieRepository movieRepository;
    private final MovieMapper mapper;

    public void index(MovieDocument document) {
        try {
            searchRepository.index(document);
        } catch (Exception ex) {
            log.error("Error indexing document: {}", document.getId(), ex);
        }
    }

    public List<MovieResponse> searchMovies(String title,
                                            String genre,
                                            Double minRating,
                                            Double maxRating) {
        try {
            List<MovieDocument> documents = searchRepository.search(
                    title, genre, minRating, maxRating);

            return documents.stream()
                    .map(doc -> MovieResponse.builder()
                            .id(doc.getId())
                            .title(doc.getTitle())
                            .genre(doc.getGenre())
                            .releaseYear(doc.getReleaseYear())
                            .rating(doc.getRating())
                            .cast(doc.getCast())
                            .build()).toList();
        } catch (Exception ex) {
            log.error("Error getting movies", ex);
        }
        return Collections.emptyList();
    }

    public void deleteMovie(String id) {
        try {
            searchRepository.deleteById(id);
        } catch (Exception ex) {
            log.error("Error deleting document with id: {}", id, ex);
        }

    }

    public List<MovieResponse> recommend(String movieId, Integer limit) {
        try {
            Movie movie = movieRepository.findById(movieId)
                    .orElseThrow(() ->
                            new RuntimeException("Movie not found"));

            if (limit == null) limit = 5;

            List<MovieDocument> docs = searchRepository.recommend(
                    movie.getId(),
                    movie.getGenre(),
                    limit
            );

            return docs.stream()
                    .map(d -> MovieResponse.builder()
                            .id(d.getId())
                            .title(d.getTitle())
                            .genre(d.getGenre())
                            .rating(d.getRating())
                            .releaseYear(d.getReleaseYear())
                            .build()
                    )
                    .toList();
        } catch (Exception ex) {
            log.error("Exception finding recommendation for id: {}", movieId, ex);
        }

        return Collections.emptyList();
    }
}

