package com.smile.review.controller;

import com.smile.review.dto.responsedto.LikeResponseDto;
import com.smile.review.service.LikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Map;

/**
 * 리뷰 좋아요 토글 기능
 */
@RestController
@RequestMapping("/reviews/{reviewId}/like")
public class LikeController {

    private final LikeService likeService;

    @Autowired
    public LikeController(LikeService likeService) {
        this.likeService = likeService;
    }

    /**
     * 리뷰 좋아요 토글
     * POST /reviews/{reviewId}/like
     *
     * @param reviewId ID
     * @param principal 로그인 사용자 정보
     * @return 현재 좋아요 상태 및 총 좋아요 수 (또는 간단히 상태코드)
     */
    @PostMapping("/{reviewId}/like")
    public ResponseEntity<Map<String, Object>> toggleLike(
            @PathVariable Long reviewId,
            Principal principal) {

        String userName = principal.getName();
        likeService.likeReview(reviewId, userName);

        // 좋아요 후 상태와 개수를 반환
        boolean liked = likeService.hasLiked(reviewId, userName);
        long totalLikes = likeService.countLikes(reviewId);

        return ResponseEntity.ok(Map.of(
                "liked", liked,
                "totalLikes", totalLikes
        ));
    }

    /**
     * (선택) 특정 리뷰의 좋아요 정보 조회
     * GET /reviews/{reviewId}/likes
     */
    @GetMapping("/{reviewId}/likes")
    public ResponseEntity<Map<String, Object>> getLikeInfo(
            @PathVariable Long reviewId,
            Principal principal) {

        long totalLikes = likeService.countLikes(reviewId);
        Boolean liked = null;
        if (principal != null) {
            liked = likeService.hasLiked(reviewId, principal.getName());
        }
        return ResponseEntity.ok(Map.of(
                "totalLikes", totalLikes,
                "liked", liked
        ));
    }

// 추가적으로 댓글 목록 조회, 리뷰 수정/삭제, 댓글 수정/삭제, 좋아요 취소 DELETE 엔드포인트 등 필요 시 구현 가능
}
