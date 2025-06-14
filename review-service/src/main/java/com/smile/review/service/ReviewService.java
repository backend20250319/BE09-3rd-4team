package com.smile.review.service;

import com.smile.review.client.MovieClient;
import com.smile.review.client.UserClient;

import com.smile.review.client.dto.MovieDto;
import com.smile.review.client.dto.UserDto;

import com.smile.review.domain.Review;
import com.smile.review.dto.responsedto.ReviewResponseDto;

import com.smile.review.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ReviewService {

    private MovieClient movieClient;
    private UserClient userClient;
    private ReviewRepository reviewRepository;
    // ... 기타 의존성 주입

    @Autowired
    public ReviewService(
                         MovieClient movieClient,
                         UserClient userClient,
                         ReviewRepository reviewRepository /*, ... */) {
        this.movieClient = movieClient;
        this.userClient = userClient;
        this.reviewRepository = reviewRepository;
    }

    public ReviewService() {
    }

    public ReviewResponseDto createReview(Long userId, Long movieId, String content, int rating) {
        // 1) 사용자 검증
        UserDto userDto = userClient.getUserId(userId).getData();

        // 2) 영화 검증
        MovieDto movieDto = movieClient.getMovieId(movieId).getData();
        if (movieDto == null || movieDto.getMovieId() == null) {
            throw new IllegalArgumentException("유효하지 않은 영화 ID");
        }

        // 3) 리뷰 엔티티 저장 로직
        Review review = new Review();
        review.setUserId(userDto.getUserId());
        review.setMovieId(movieDto.getMovieId());
        review.setContent(content);
        review.setRating(rating);
        review.setCreatedAt(LocalDateTime.now());
        Review saved = reviewRepository.save(review);

        // 4) DTO 변환 후 반환
        return ReviewResponseDto.fromEntity(saved, userDto.getUsername(), movieDto.getTitle());
    }

}


