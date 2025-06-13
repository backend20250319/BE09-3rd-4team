package com.smile.review.repository.like;

import com.smile.review.domain.ReviewLike;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewLikeRepository extends JpaRepository<ReviewLike, Long> {
    /**
     * 특정 리뷰에 대해 사용자가 이미 좋아요를 눌렀는지 확인
     */
    boolean existsByReviewIdAndUserId(Long reviewId, Long userId);

    /**
     * 특정 리뷰의 좋아요 개수 조회
     */
    Long countByReviewId(Long reviewId);

    /**
     * 특정 리뷰에 대한 특정 사용자의 좋아요 삭제(취소)
     */
    void deleteByReviewIdAndUserId(Long reviewId, Long userId);

    /**
     * 예: 특정 사용자 좋아요 목록 조회
     */
    // List<ReviewLike> findByUserId(Long userId);
}
