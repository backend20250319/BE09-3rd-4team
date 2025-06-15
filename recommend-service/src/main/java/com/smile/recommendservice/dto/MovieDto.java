package com.smile.recommendservice.dto;

import lombok.*;

import java.time.LocalDate;
import java.util.List;
//영화 정보 전송용 DTO
/***
 * 추천 결과로 내려줄 영화 정보 (search-service의 SearchDTO와 같은 구조를 가진 필드)
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MovieDto {
    private Long id;
    private String title;
    private String description;
    private String ageRating;
    private List<String> genres;
    private Double rating;
    private String releaseDate;
    private String directorName;
    private List<String> actors;
}

