package com.smile.movieservice.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MovieRequest {
    private String title;
    private String overview;
    private String genre;
    private Double rating;
    private String releaseDate;
    private String posterUrl;
}