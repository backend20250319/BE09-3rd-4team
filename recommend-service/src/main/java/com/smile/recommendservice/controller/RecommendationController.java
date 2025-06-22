package com.smile.recommendservice.controller;

import com.smile.recommendservice.common.ApiResponse;
import com.smile.recommendservice.domain.dto.RecommendationResultDto;
import com.smile.recommendservice.domain.dto.UserDetailsWrapper;
import com.smile.recommendservice.dto.UserDto;
import com.smile.recommendservice.repository.UserClient;
import com.smile.recommendservice.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/recommendations")
@RequiredArgsConstructor
public class RecommendationController {

    private final AgeBasedRecommendationService ageService;
    private final GenderBasedRecommendationService genderService;
    private final CombinedRecommendationService combinedService;
    private final UserClient userClient;


    // 로그인 사용자 정보 추출 공통 메서드
    // FeignClient를 통해 user-service에서 진짜 gender 정보 포함된 유저 객체를 가져오도록 바꿈
    private UserDto getCurrentUser() {
        ApiResponse<UserDetailsWrapper> response = userClient.getCurrentUserInfo();
        return response.getData().getUser(); // 이 user는 gender 포함됨!
    }


    @GetMapping("/by-age")
    public ResponseEntity<RecommendationResultDto> recommendByAge() {
        UserDto user = getCurrentUser();
        return ResponseEntity.ok(ageService.recommend(new UserDetailsWrapper(user)));
    }

    @GetMapping("/by-gender")
    public ResponseEntity<RecommendationResultDto> recommendByGender() {
        UserDto user = getCurrentUser();
        return ResponseEntity.ok(genderService.recommend(new UserDetailsWrapper(user)));
    }

    @GetMapping("/by-combined")
    public ResponseEntity<RecommendationResultDto> recommendByCombined() {
        UserDto user = getCurrentUser();
        return ResponseEntity.ok(combinedService.recommend(new UserDetailsWrapper(user)));
    }
}
