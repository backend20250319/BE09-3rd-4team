package com.smile.review.client;

import com.smile.review.client.dto.MovieDto;
import com.smile.review.common.ApiResponse;
import com.smile.review.config.FeignClientConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "movie-service", configuration = FeignClientConfig.class)
public interface MovieClient {


    @GetMapping("/movies/fetchAll/{movieId}")
    ApiResponse<MovieDto> getMovieId(@PathVariable("movieId") Long movieId);
}
