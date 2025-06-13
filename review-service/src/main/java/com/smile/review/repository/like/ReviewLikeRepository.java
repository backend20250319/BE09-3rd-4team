package com.smile.review.repository.like;

import com.smile.review.domain.ReviewLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReviewLikeRepository extends JpaRepository<ReviewLike,Long> {

    Optional<ReviewLike> findByReviewIdAndUserId(Long reviewId, Long userId);

    long countByReviewId(Long reviewId);
}
