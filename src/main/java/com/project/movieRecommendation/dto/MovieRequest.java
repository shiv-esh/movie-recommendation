package com.project.movieRecommendation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MovieRequest {

    private String title;
    private String genre;
    private Integer releaseYear;
    private Double rating;
    private Double maxRating;
    private List<String> cast;

}

