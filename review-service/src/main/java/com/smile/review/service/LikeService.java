package com.smile.review.service;

//import com.smile.review.client.UserClient;
import com.smile.review.domain.ReviewLike;
import com.smile.review.dto.responsedto.LikeResponseDto;

import com.smile.review.repository.ReviewRepository;
import com.smile.review.repository.like.ReviewLikeRepository;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class LikeService {

    private final ReviewLikeRepository reviewLikeRepository;
    private final ReviewRepository reviewRepository;
//    private final UserClient userClient;

    @Autowired
    public LikeService(ReviewLikeRepository reviewLikeRepository,
                       ReviewRepository reviewRepository
//                       UserClient userClient
    ) {
        this.reviewLikeRepository = reviewLikeRepository;
        this.reviewRepository = reviewRepository;
//        this.userClient = userClient;
    }

    /**
     * 좋아요 토글: 이미 좋아요가 있으면 취소, 없으면 생성
     * @param userIdFromToken 인증된 사용자 ID
     * @param reviewId 좋아요 대상 리뷰 ID
     * @return LikeResponseDto
     */
    @Transactional
    @CircuitBreaker(name = "userServiceCircuit", fallbackMethod = "fallbackToggleLike")
    @Retry(name = "userServiceRetry")
   /* public LikeResponseDto toggleLike(Long userIdFromToken, Long reviewId) {
        if (!reviewRepository.existsById(reviewId)) {
            throw new IllegalArgumentException("해당 리뷰가 없습니다: " + reviewId);
        }
        boolean exists = reviewLikeRepository.existsByReviewIdAndUserId(reviewId, userIdFromToken);
        if (exists) {
            // 이미 좋아요 -> 삭제
            reviewLikeRepository.deleteByReviewIdAndUserId(reviewId, userIdFromToken);
        } else {
            // 좋아요 생성
            ReviewLike like = ReviewLike.builder()
                    .reviewId(reviewId)
                    .userId(userIdFromToken)
                    .createdAt(LocalDateTime.now())
                    .build();
            reviewLikeRepository.save(like);
        }
        Long likeCount = reviewLikeRepository.countByReviewId(reviewId);
        return LikeResponseDto.builder()
                .reviewId(reviewId)
                .likeCount(likeCount)
                .liked(!exists)
                .build();
    }
*/
    public LikeResponseDto fallbackToggleLike(Long userIdFromToken, Long reviewId, Throwable t) {
        throw new RuntimeException("좋아요 처리 중 오류 발생: " + t.getMessage());
    }

    public long countLikes(Long reviewId) {
    }

    public Boolean hasLiked(Long reviewId, String name) {
    }
}
