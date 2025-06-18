package com.smile.review.service;

import com.smile.review.client.MovieClient;
import com.smile.review.client.UserClient;

import com.smile.review.client.dto.MovieDto;
import com.smile.review.client.dto.UserDto;

import com.smile.review.domain.Review;
import com.smile.review.dto.StarRatingDto;
import com.smile.review.dto.requestdto.ReviewRequestDto;
import com.smile.review.dto.responsedto.ReviewResponseDto;


import com.smile.review.repository.review.ReviewRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;


public interface ReviewService {

    ReviewResponseDto getReviewId(Long reviewId, String userId, Long movieId);

    MovieClient movieClient = null;
    UserClient userClient = null;
    ReviewRepository reviewRepository = null;
    // ... 기타 의존성 주입


    public default ReviewResponseDto createReview(String userId, Long movieId, String content, double rating) {
        // 1) 사용자 검증
        UserDto userDto = userClient.getUserId(userId).getData().getUser();

        // 2) 영화 검증
        MovieDto movieDto = movieClient.getMovieId(movieId).getData();
        if (movieDto == null || movieDto.getId() == null) {
            throw new IllegalArgumentException("유효하지 않은 영화 ID");
        }

        // 3) 리뷰 엔티티 저장 로직
        Review review = new Review();
        review.setUserId(userDto.getUserId());
        review.setMovieId(movieDto.getId());
        review.setContent(content);
        review.setRating(rating);
        review.setCreatedAt(LocalDateTime.now());
        Review saved = reviewRepository.save(review);

        // 4) DTO 변환 후 반환
        return ReviewResponseDto.fromEntity(saved, userDto.getUserId(), movieDto.getTitle());
    }

    @Transactional(readOnly = true)
    Page<ReviewResponseDto> listReviews(int page, int size, String sortBy);

    @Transactional
    Page<ReviewResponseDto> findReviews(String genre, Double rating, String sort, Pageable pageable);

    @Transactional
    ReviewResponseDto editReview(Long reviewId, String userName, ReviewRequestDto dto);

    @Transactional
    void deleteReview(Long reviewId, String userId);

    List<StarRatingDto> getByAgeGroup(String ageGroup);

    List<StarRatingDto> getByGender(String gender);

    List<StarRatingDto> getByAgeAndGender(String ageGroup, String gender);

}


