package com.smile.review.client;

import com.smile.review.client.dto.MovieDto;
import com.smile.review.common.ApiResponse;
import com.smile.review.config.FeignClientConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "movie-service", configuration = FeignClientConfig.class)
public interface MovieClient {


    @GetMapping("/movies/fetchAll/{movieId}")
    ApiResponse<MovieDto> getMovieId(@PathVariable("movieId") Long movieId);

    @PutMapping("/{movieId}/update-average-rating")
    void updateAverageRating(@PathVariable("movieId") Long movieId);



}
