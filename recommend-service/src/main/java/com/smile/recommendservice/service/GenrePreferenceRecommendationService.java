package com.smile.recommendservice.service;

import com.smile.recommendservice.domain.dto.RecommendationResultDto;
import com.smile.recommendservice.domain.service.RecommendationPolicy;
import com.smile.recommendservice.domain.type.RecommendationType;
import com.smile.recommendservice.dto.MovieDto;
import com.smile.recommendservice.dto.StarRatingDto;
import com.smile.recommendservice.dto.UserDto;
import com.smile.recommendservice.domain.dto.UserDetailsWrapper;
import com.smile.recommendservice.repository.MovieClient;
import com.smile.recommendservice.repository.ReviewClient;
import com.smile.recommendservice.repository.UserClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

// 사용자가 선호하는 장르를 추출해서 그 장르의 영화 중 평점 높은 영화 추천
@Service
@RequiredArgsConstructor
public class GenrePreferenceRecommendationService implements RecommendationPolicy {

    private final ReviewClient reviewClient;
    private final MovieClient movieClient;
    private final UserClient userClient;

        @Override
        public RecommendationResultDto recommend(UserDetailsWrapper userWrapper) {
        // 현재 사용자 정보
        UserDto user = userWrapper.getUser();
        String userId = user.getUserId();

        // 사용자가 남긴 리뷰 정보
        List<StarRatingDto> ratings = reviewClient.getByUserId(user.getUserId()).getData();

        // 0개 → 예외 처리
        if (ratings.isEmpty()) {
            throw new IllegalStateException("별점을 등록한 영화가 없습니다.");
        }

        // 1개 → 해당 영화의 대표 장르 Top 10 추천
        if (ratings.size() == 1) {
            MovieDto movie = movieClient.getMovieById(ratings.get(0).getMovieId());
            String genre = movie.getGenres().get(0);
            return movieClient.getTopRatedMoviesByGenre(genre, 10);
        }

        // 2개 → 장르가 같으면 Top10, 다르면 각 장르 Top5
        if (ratings.size() == 2) {
            MovieDto movie1 = movieClient.getMovieById(ratings.get(0).getMovieId());
            MovieDto movie2 = movieClient.getMovieById(ratings.get(1).getMovieId());

            List<String> genres1 = movie1.getGenres();
            List<String> genres2 = movie2.getGenres();

            Set<String> common = new HashSet<>(genres1);
            common.retainAll(genres2);

            if (!common.isEmpty()) {
                String selected = common.stream().sorted().findFirst().orElse(genres1.get(0));
                return movieClient.getTopRatedMoviesByGenre(selected, 10);
            } else {
                String genre1 = genres1.get(0);
                String genre2 = genres2.get(0);
                List<MovieDto> list1 = movieClient.getTopRatedMoviesByGenre(genre1, 5);
                List<MovieDto> list2 = movieClient.getTopRatedMoviesByGenre(genre2, 5);
                List<MovieDto> merged = mergeWithoutDuplicates(list1, list2);

                return RecommendationResultDto.builder()
                        .recommendationType(RecommendationType.GENRE_BASED)
                        .criteria("선호 장르 기반")
                        .generatedAt(LocalDateTime.now())
                        .movies(merged)
                        .build();

            }
        }

        // 3개 이상 → 장르별 평균 평점 계산 (모든 장르 반영)
        Map<String, List<Double>> genreToRatings = new HashMap<>();

        for (StarRatingDto rating : ratings) {
            MovieDto movie = movieClient.getMovieById(rating.getMovieId());
            for (String genre : movie.getGenres()) {
                genreToRatings.computeIfAbsent(genre, k -> new ArrayList<>()).add(rating.getStar());            }
        }

        // 장르별 평균 평점 + 정렬 (평점 > 리뷰 수 > 알파벳순)
        List<Map.Entry<String, Double>> sortedGenres = genreToRatings.entrySet().stream()
                .map(entry -> Map.entry(entry.getKey(), average(entry.getValue())))
                .sorted((a, b) -> {
                    int cmp = Double.compare(b.getValue(), a.getValue());
                    if (cmp != 0) return cmp;
                    int countA = genreToRatings.get(a.getKey()).size();
                    int countB = genreToRatings.get(b.getKey()).size();
                    if (countA != countB) return countB - countA;
                    return a.getKey().compareTo(b.getKey());
                })
                .collect(Collectors.toList());

        // 상위 3개 장르 → 각각 4, 3, 3개 추천
        List<String> topGenres = sortedGenres.stream()
                .limit(3)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        int[] counts = {4, 3, 3};
        List<MovieDto> recommendations = new ArrayList<>();

        for (int i = 0; i < topGenres.size(); i++) {
            String genre = topGenres.get(i);
            List<MovieDto> topMovies = movieClient.getTopRatedMoviesByGenre(genre, counts[i]);
            recommendations.addAll(topMovies);
        }

        // 영화 ID 기준으로 중복 제거
            List<MovieDto> finalList = recommendations.stream()
                    .collect(Collectors.collectingAndThen(
                            Collectors.toMap(MovieDto::getId, m -> m, (m1, m2) -> m1, LinkedHashMap::new),
                            m -> new ArrayList<>(m.values())
                    ));

            return RecommendationResultDto.builder()
                    .recommendationType(RecommendationType.GENRE_BASED)
                    .criteria("선호 장르 기반")
                    .generatedAt(LocalDateTime.now())
                    .movies(finalList)
                    .build();
    }

    private double average(List<Double> ratings) {
        return ratings.stream().mapToDouble(Double::doubleValue).average().orElse(0);
    }

    private List<MovieDto> mergeWithoutDuplicates(List<MovieDto> list1, List<MovieDto> list2) {
        Map<Long, MovieDto> map = new LinkedHashMap<>();
        for (MovieDto movie : list1) map.put(movie.getId(), movie);
        for (MovieDto movie : list2) map.put(movie.getId(), movie);
        return new ArrayList<>(map.values());
    }
}
