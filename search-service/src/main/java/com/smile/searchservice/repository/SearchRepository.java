package com.smile.searchservice.repository;

import com.smile.searchservice.entity.Movie;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface SearchRepository extends JpaRepository<Movie, Long> {

    // 배우 이름으로 영화 검색
    List<Movie> findByActor(String actor);

    // 감독 이름으로 영화 검색
    List<Movie> findByDirector(String director);

    // 영화 제목으로 검색
    List<Movie> findByTitle(String title);

    // 장르로 영화 검색
    List<Movie> findByGenre(String genre);

    // 장르별 평점 내림차순 영화 조회
    List<Movie> findByGenreNameOrderByRatingDesc(String genre);

    // 평점 내림차순으로 모든 영화 조회
    List<Movie> findAllByOrderByRatingDesc();

    // 개봉일 내림차순으로 모든 영화 조회
    List<Movie> findAllByOrderByReleaseDateDesc();

}