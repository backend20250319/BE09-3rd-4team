package com.smile.recommendservice.service;

import com.smile.recommendservice.dto.StarRatingDto;
import com.smile.recommendservice.dto.UserDto;
import com.smile.recommendservice.repository.ReviewClient;
import com.smile.recommendservice.repository.UserClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
// 연령대 기반 추천 서비스
//
// UserClient + ReviewClient를 이용해 추천 영화 ID 리스트 반환
@Service
@RequiredArgsConstructor
public class AgeBasedRecommendationService {

    private final ReviewClient reviewClient;
    private final UserClient userClient;

    public List<Long> recommendByAge(Long userId) {
        // 1. 유저 정보 조회
        UserDto userDto = userClient.getUserInfo(userId);
        String ageGroup = userDto.getAgeGroup();

        // 2. 같은 연령대 유저들의 별점 정보 가져오기
        List<StarRatingDto> starRatings = reviewClient.getByAge(age);

        // 3. 본인 제외
        starRatings = starRatings.stream()
                .filter(rating -> !rating.getUserId().equals(userId))
                .collect(Collectors.toList());

        // 4. 영화별 평점 리스트 모으기
        Map<Long, List<Double>> ratingMap = new HashMap<>();
        for (StarRatingDto rating : starRatings) {
            ratingMap
                    .computeIfAbsent(rating.getMovieId(), k -> new ArrayList<>())
                    .add(rating.getStar());
        }

        // 5. 평균 평점 계산 후 정렬 → Top 10 영화 ID 반환
        return ratingMap.entrySet().stream()
                .map(entry -> Map.entry(
                        entry.getKey(),
                        entry.getValue().stream().mapToDouble(Double::doubleValue).average().orElse(0.0)
                ))
                .sorted((a, b) -> Double.compare(b.getValue(), a.getValue())) // 내림차순
                .limit(10)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }
}




