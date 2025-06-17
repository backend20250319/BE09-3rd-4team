package com.smile.review.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StarRatingDto {
    private Long movieId;
    private String userId;
    private Double rating;

    public StarRatingDto(Long movieId, double rating) {
        this.movieId = movieId;
        this.rating = rating;
    }
}