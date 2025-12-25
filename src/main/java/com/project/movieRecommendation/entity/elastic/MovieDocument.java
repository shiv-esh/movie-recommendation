package com.project.movieRecommendation.entity.elastic;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MovieDocument {

    private String id;
    private String title;
    private String genre;
    private Integer releaseYear;
    private Double rating;
    private List<String> cast;
}
