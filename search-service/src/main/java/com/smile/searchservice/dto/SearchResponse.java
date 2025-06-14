package com.smile.searchservice.dto;

import com.smile.searchservice.entity.Movie;
import lombok.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter; // 필요한 임포트

/**
 * 서버에서 클라이언트로 반환하는 응답 객체
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SearchResponse {
    private Long id;
    private String title;
    private String genre;
    private String director;
    private String actor;
    private String description;
    private Double rating;
    private String releaseDate; // LocalDate에서 String으로 타입 변경

    // Movie 엔티티를 SearchResponse로 변환하는 메서드
    public static SearchResponse from(Movie movie) {
        return SearchResponse.builder()
                .id(movie.getId())
                .title(movie.getTitle())
                .genre(movie.getGenre().getName())  // 장르에서 이름만 가져오기
                .director(movie.getDirector().getName())  // 감독 이름
                .actor(movie.getActor().getName())  // 배우 이름
                .description(movie.getDescription())
                .rating(movie.getRating())
                // LocalDate를 String으로 변환 (예: "yyyy-MM-dd" 형식)
                // movie.getReleaseDate()가 null이 아닌 경우에만 포맷팅
                .releaseDate(movie.getReleaseDate() != null ? movie.getReleaseDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) : null)
                .build();
    }
}