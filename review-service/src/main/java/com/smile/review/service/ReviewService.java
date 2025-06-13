package com.smile.review.service;

import com.smile.review.domain.Review;
import com.smile.review.dto.responsedto.ReviewResponseDto;

import com.smile.review.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ReviewService {

    private final MovieClient movieClient;
    private final UserClient userClient;
    private final ReviewRepository reviewRepository;
    // ... 기타 의존성 주입

    @Autowired
    public ReviewService(
//            MovieClient movieClient,
//                         UserClient userClient,
                         ReviewRepository reviewRepository /*, ... */) {
//        this.movieClient = movieClient;
//        this.userClient = userClient;
        this.reviewRepository = reviewRepository;
    }

    public ReviewResponseDto createReview(String userName, Long movieId, String content, int rating) {
        // 1) 사용자 검증
        UserDto userDto;

        try {
            userDto = userClient.getUserByName(userName);
        } catch (Exception ex) {
            // fallbackFactory에서 예외 던졌다면 여기로 진입
            throw new IllegalArgumentException("사용자 정보를 가져올 수 없습니다.", ex);
        }

        // 2) 영화 검증
        MovieDto movieDto = movieClient.getMovieById(movieId);
        if (movieDto == null || movieDto.getId() == null) {
            throw new IllegalArgumentException("유효하지 않은 영화 ID");
        }

        // 3) 리뷰 엔티티 저장 로직
        Review review = new Review();
        review.setUserId(userDto.getId());
        review.setMovieId(movieDto.getId());
        review.setContent(content);
        review.setRating(rating);
        review.setCreatedAt(LocalDateTime.now());
        Review saved = reviewRepository.save(review);

        // 4) DTO 변환 후 반환
        return ReviewResponseDto.fromEntity(saved, userDto, movieDto);
    }

    public ReviewResponseDto getReviewById(Long reviewId) {
    }

    public Page<ReviewResponseDto> findReviews(String genre, Integer minRating, String sortBy, Pageable pageable) {
    }
}


