package com.smile.recommendservice.repository;

import com.smile.recommendservice.common.ApiResponse;
import com.smile.recommendservice.config.FeignAuthConfig;
import com.smile.recommendservice.domain.dto.UserDetailsWrapper;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/* 로그인한 사용자의 성별, 연령대, 선호 장르 등 유저 정보를 가져오는 용도

추천 정책을 결정할 때 "이 사용자는 어떤 기준으로 추천해야 하지?" 판단의 근거가 됨

이 데이터를 기반으로 → 성별 기반, 연령 기반, 장르 기반 등 정책 선택 */

// user-service와 통신하는 FeignClient
@FeignClient(name = "user-service", configuration = FeignAuthConfig.class)
public interface UserClient {
    @GetMapping("/users/me")
    ApiResponse<UserDetailsWrapper> getCurrentUserInfo();
}

