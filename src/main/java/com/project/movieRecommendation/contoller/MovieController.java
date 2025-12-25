package com.project.movieRecommendation.contoller;

import com.project.movieRecommendation.dto.MovieRequest;
import com.project.movieRecommendation.dto.MovieResponse;
import com.project.movieRecommendation.service.MovieSearchService;
import com.project.movieRecommendation.service.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/movies")
@RequiredArgsConstructor
public class MovieController {

    private final MovieService movieService;
    private final MovieSearchService movieSearchService;

    @PostMapping
    public ResponseEntity<MovieResponse> createMovie(
            @RequestBody MovieRequest request) throws IOException {

        return ResponseEntity.ok(movieService.createMovie(request));
    }

    @GetMapping
    public ResponseEntity<List<MovieResponse>> getAllMovies() {
        return ResponseEntity.ok(movieService.getAllMovies());
    }

    @GetMapping("/search")
    public ResponseEntity<List<MovieResponse>> searchMovies(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String genre,
            @RequestParam(required = false) Double minRating,
            @RequestParam(required = false) Double maxRating
    ) {

        return ResponseEntity.ok(movieSearchService.searchMovies(title, genre, minRating, maxRating));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteMovie(@PathVariable String id) {
        movieService.deleteMovie(id);
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/recommendations")
    public ResponseEntity<List<MovieResponse>> recommend(
            @RequestParam String movieId,
            @RequestParam(defaultValue = "5") Integer limit
    ) {


        return ResponseEntity.ok(movieSearchService.recommend(movieId,limit));
    }

}
