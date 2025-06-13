package com.smile.searchservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "MOVIE-SERVICE")  // 'movie-service'는 실제 movie-service의 이름이어야 합니다.
public interface MovieClient {

    // 영화 정보 조회
    @GetMapping("/movies/{id}")  // movie-service의 실제 엔드포인트 URI
    MovieDTO getMovieById(@PathVariable("id") Long id);  // PathVariable로 ID를 받아 조회
}