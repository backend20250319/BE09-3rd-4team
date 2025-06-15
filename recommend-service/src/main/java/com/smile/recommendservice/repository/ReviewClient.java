package com.smile.recommendservice.repository;

import com.smile.recommendservice.common.ApiResponse;
import org.springframework.cloud.openfeign.FeignClient;

import com.smile.recommendservice.dto.StarRatingDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.List;

// review-service와 통신하는 FeignClient
// 별점 데이터 조회

@FeignClient(name = "review-service", path = "/reviews")
public interface ReviewClient {

    @GetMapping("/stars/by-age-group")
    List<StarRatingDto> getByAgeGroup(@RequestParam String ageGroup);

    @GetMapping("/stars/by-gender")
    List<StarRatingDto> getByGender(@RequestParam String gender);

    @GetMapping("/stars/by-age-and-gender")
    List<StarRatingDto> getByAgeAndGender(@RequestParam String ageGroup,
                                          @RequestParam String gender);

    @GetMapping("/by-user-id")
    ApiResponse<List<StarRatingDto>> getByUserId(@RequestParam String userId);

}
