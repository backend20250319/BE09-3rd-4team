package com.smile.searchservice.dto;

import com.smile.searchservice.entity.Actor;
import com.smile.searchservice.entity.Director;
import com.smile.searchservice.entity.Genre;
import lombok.*;

import java.time.LocalDate;

/**
 * 클라이언트가 영화를 등록하거나 수정할 때 사용하는 요청 객체
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SearchRequest {
    private String title;
    private Genre genre;
    private Director director;
    private Actor actor;
    private String description;
    private Double rating;
    private LocalDate releaseDate;
}