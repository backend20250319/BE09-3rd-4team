package com.smile.recommendservice.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
//영화 정보 전송용 DTO
/***
 * 추천 결과로 내려줄 영화 정보 (search-service의 SearchDTO와 같은 구조를 가진 필드)
 */
@Getter
@Setter
public class MovieDto {
    private Long id;
    private String title;
    private String genre;
    private String director;
    private String actor;
    private String description;
    private Double rating;
    private LocalDate releaseDate;
}
