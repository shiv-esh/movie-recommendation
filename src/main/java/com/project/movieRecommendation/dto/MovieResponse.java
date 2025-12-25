package com.project.movieRecommendation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MovieResponse {

    private String id;
    private String title;
    private String genre;
    private Integer releaseYear;
    private Double rating;
    private List<String> cast;

}
