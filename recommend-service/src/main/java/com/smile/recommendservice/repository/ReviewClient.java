package com.smile.recommendservice.repository;

import com.smile.recommendservice.common.ApiResponse;
import org.springframework.cloud.openfeign.FeignClient;

import com.smile.recommendservice.dto.StarRatingDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.List;

// review-service와 통신하는 FeignClient
// 별점 데이터 조회

@FeignClient(name = "review-service", path = "/reviews")
public interface ReviewClient {

    @GetMapping("/stars/age-group/{ageGroup}")
    List<StarRatingDto> getByAgeGroup(@PathVariable String ageGroup);

    @GetMapping("/stars/gender/{gender}")
    List<StarRatingDto> getByGender(@PathVariable String gender);

    @GetMapping("/stars/age-group/{ageGroup}/gender/{gender}")
    List<StarRatingDto> getByAgeAndGender(@PathVariable String ageGroup,
                                          @PathVariable String gender);

    @GetMapping("/user/{userId}")
    ApiResponse<List<StarRatingDto>> getByUserId(@PathVariable String userId);


}
