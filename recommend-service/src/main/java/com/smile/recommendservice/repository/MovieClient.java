package com.smile.recommendservice.repository;

import com.smile.recommendservice.dto.MovieDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "movie-service", path = "/movies")
public interface MovieClient {

    @GetMapping("/fetchAll/{movieId}")
    MovieDto getMovieById(@PathVariable("movieId") Long movieId);

}
