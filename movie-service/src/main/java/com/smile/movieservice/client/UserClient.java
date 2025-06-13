package com.smile.movieservice.client;

import com.smile.movieservice.common.ApiResponse;
import com.smile.movieservice.config.FeignClientConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

// 1. Gateway 를 호출하는 상황
// @FeignClient(name = "user-service", url = "http://localhost:8000", configuration = FeignClientConfig.class)
// 2. 내부에서 user-service를 호출하는 상황
@FeignClient(name = "smile-movie-service", configuration = FeignClientConfig.class)
public interface UserClient {

    // User Service에서 사용자 상태나 간단한 정보를 조회하는 API
    // 1. Gateway를 호출하는 상황
//    @GetMapping("/api/v1/user-service/users/{userId}/grade")

    // 2. 내부에서 user-service를 호출하는 상황
    @GetMapping("/users/{userId}/grade")
    ApiResponse<String> getUserGrade(@PathVariable("userId") Long userId);
}
