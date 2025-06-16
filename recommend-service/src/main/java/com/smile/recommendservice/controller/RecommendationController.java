package com.smile.recommendservice.controller;

import com.smile.recommendservice.domain.dto.RecommendationResultDto;
import com.smile.recommendservice.domain.dto.UserDetailsWrapper;
import com.smile.recommendservice.dto.UserDto;
import com.smile.recommendservice.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
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
    private final GenrePreferenceRecommendationService genreService;

    // 로그인 사용자 정보 추출 공통 메서드
    private UserDto getCurrentUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (!(principal instanceof Map<?, ?> principalMap)) {
            throw new IllegalStateException("사용자 정보가 없습니다. 로그인 필요.");
        }

        String userId = (String) principalMap.get("userId");
        String gender = (String) principalMap.get("gender");
        Integer age = (Integer) principalMap.get("age");

        return UserDto.builder()
                .userId(userId)
                .gender(gender)
                .age(age)
                .build();
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

    @GetMapping("/by-genre")
    public ResponseEntity<RecommendationResultDto> recommendByGenre() {
        UserDto user = getCurrentUser();
        return ResponseEntity.ok(genreService.recommend(new UserDetailsWrapper(user)));
    }
}
