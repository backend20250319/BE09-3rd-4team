//package com.smile.recommendservice.service;
//
//import com.smile.recommendservice.domain.dto.RecommendationResultDto;
//import com.smile.recommendservice.domain.service.RecommendationPolicy;
//import com.smile.recommendservice.domain.type.RecommendationType;
//import com.smile.recommendservice.dto.MovieDto;
//import com.smile.recommendservice.dto.MovieResponse;
//import com.smile.recommendservice.dto.StarRatingDto;
//import com.smile.recommendservice.dto.UserDto;
//import com.smile.recommendservice.domain.dto.UserDetailsWrapper;
//import com.smile.recommendservice.repository.MovieClient;
//import com.smile.recommendservice.repository.ReviewClient;
//import com.smile.recommendservice.repository.UserClient;
//import com.smile.recommendservice.common.ApiResponse;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//
//import java.time.LocalDateTime;
//import java.util.*;
//import java.util.stream.Collectors;
//
//@Service
//@RequiredArgsConstructor
//public class GenrePreferenceRecommendationService implements RecommendationPolicy {
//
//    private final ReviewClient reviewClient;
//    private final MovieClient movieClient;
//    private final UserClient userClient;
//
//    @Override
//    public RecommendationResultDto recommend(UserDetailsWrapper userWrapper) {
//        UserDto user = userWrapper.getUser();
//        String userId = user.getUserId();
//
//        List<StarRatingDto> ratings = reviewClient.getByUserId(userId).getData();
//
//        if (ratings.isEmpty()) {
//            throw new IllegalStateException("별점을 등록한 영화가 없습니다.");
//        }
//
//        if (ratings.size() == 1) {
//            MovieDto movie = toDto(movieClient.getMovieById(ratings.get(0).getMovieId()).getData());
//            String genre = movie.getGenres().get(0);
//            List<MovieDto> result = getTopRatedMoviesByGenreLocally(genre, 10);
//            return buildResult(result);
//        }
//
//        if (ratings.size() == 2) {
//            MovieDto movie1 = toDto(movieClient.getMovieById(ratings.get(0).getMovieId()).getData());
//            MovieDto movie2 = toDto(movieClient.getMovieById(ratings.get(1).getMovieId()).getData());
//
//            List<String> genres1 = movie1.getGenres();
//            List<String> genres2 = movie2.getGenres();
//
//            Set<String> common = new HashSet<>(genres1);
//            common.retainAll(genres2);
//
//            if (!common.isEmpty()) {
//                String selected = common.stream().sorted().findFirst().orElse(genres1.get(0));
//                List<MovieDto> result = getTopRatedMoviesByGenreLocally(selected, 10);
//                return buildResult(result);
//            } else {
//                String genre1 = genres1.get(0);
//                String genre2 = genres2.get(0);
//                List<MovieDto> list1 = getTopRatedMoviesByGenreLocally(genre1, 5);
//                List<MovieDto> list2 = getTopRatedMoviesByGenreLocally(genre2, 5);
//                List<MovieDto> merged = mergeWithoutDuplicates(list1, list2);
//                return buildResult(merged);
//            }
//        }
//
//        Map<String, List<Double>> genreToRatings = new HashMap<>();
//
//        for (StarRatingDto rating : ratings) {
//            MovieDto movie = toDto(movieClient.getMovieById(rating.getMovieId()).getData());
//            for (String genre : movie.getGenres()) {
//                genreToRatings.computeIfAbsent(genre, k -> new ArrayList<>()).add(rating.getStar());
//            }
//        }
//
//        List<Map.Entry<String, Double>> sortedGenres = genreToRatings.entrySet().stream()
//                .map(entry -> Map.entry(entry.getKey(), average(entry.getValue())))
//                .sorted((a, b) -> {
//                    int cmp = Double.compare(b.getValue(), a.getValue());
//                    if (cmp != 0) return cmp;
//                    int countA = genreToRatings.get(a.getKey()).size();
//                    int countB = genreToRatings.get(b.getKey()).size();
//                    return countB != countA ? countB - countA : a.getKey().compareTo(b.getKey());
//                })
//                .collect(Collectors.toList());
//
//        List<String> topGenres = sortedGenres.stream()
//                .limit(3)
//                .map(Map.Entry::getKey)
//                .collect(Collectors.toList());
//
//        int[] counts = {4, 3, 3};
//        List<MovieDto> recommendations = new ArrayList<>();
//
//        for (int i = 0; i < topGenres.size(); i++) {
//            String genre = topGenres.get(i);
//            List<MovieDto> topMovies = getTopRatedMoviesByGenreLocally(genre, counts[i]);
//            recommendations.addAll(topMovies);
//        }
//
//        List<MovieDto> finalList = recommendations.stream()
//                .collect(Collectors.collectingAndThen(
//                        Collectors.toMap(MovieDto::getId, m -> m, (m1, m2) -> m1, LinkedHashMap::new),
//                        m -> new ArrayList<>(m.values())
//                ));
//
//        return buildResult(finalList);
//    }
//
//    private List<MovieDto> getTopRatedMoviesByGenreLocally(String genre, int count) {
//        List<MovieResponse> movieResponses = movieClient.getAllMovies().getData();
//
//        return movieResponses.stream()
//                .map(this::toDto)
//                .filter(movie -> movie.getGenres().contains(genre))
//                .map(movie -> {
//                    List<StarRatingDto> ratings = reviewClient.getRatingsByMovieId(movie.getId());
//                    double avg = ratings.stream().mapToDouble(StarRatingDto::getStar).average().orElse(0);
//                    movie.setAverageRating(avg);
//                    return movie;
//                })
//                .sorted(Comparator.comparingDouble(MovieDto::getAverageRating).reversed())
//                .limit(count)
//                .collect(Collectors.toList());
//    }
//
//    private MovieDto toDto(MovieResponse response) {
//        return MovieDto.builder()
//                .id(response.getId())
//                .title(response.getTitle())
//                .genres(response.getGenres())
//                .build();
//    }
//
//
//    private double average(List<Double> ratings) {
//        return ratings.stream().mapToDouble(Double::doubleValue).average().orElse(0);
//    }
//
//    private List<MovieDto> mergeWithoutDuplicates(List<MovieDto> list1, List<MovieDto> list2) {
//        Map<Long, MovieDto> map = new LinkedHashMap<>();
//        for (MovieDto movie : list1) map.put(movie.getId(), movie);
//        for (MovieDto movie : list2) map.put(movie.getId(), movie);
//        return new ArrayList<>(map.values());
//    }
//
//    private RecommendationResultDto buildResult(List<MovieDto> movies) {
//        return RecommendationResultDto.builder()
//                .recommendationType(RecommendationType.GENRE_BASED)
//                .criteria("선호 장르 기반")
//                .generatedAt(LocalDateTime.now())
//                .movies(movies)
//                .build();
//    }
//}
