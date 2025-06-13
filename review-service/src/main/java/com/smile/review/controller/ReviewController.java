package com.smile.review.controller;

import com.smile.review.dto.requestdto.CommentRequestDto;
import com.smile.review.dto.requestdto.ReviewRequestDto;
import com.smile.review.dto.responsedto.CommentResponseDto;
import com.smile.review.dto.responsedto.ReviewResponseDto;
import com.smile.review.service.CommentService;
import com.smile.review.service.LikeService;
import com.smile.review.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.security.Principal;
import java.util.Map;

@RestController
@RequestMapping("/reviews")
public class ReviewController {

    private final ReviewService reviewService;
    private final CommentService commentService;
    private final LikeService likeService;

    @Autowired
    public ReviewController(ReviewService reviewService,
                            CommentService commentService,
                            LikeService likeService) {
        this.reviewService = reviewService;
        this.commentService = commentService;
        this.likeService = likeService;
    }

    /**
     * 리뷰 작성
     * POST /reviews
     *
     * @param reviewRequestDto { movieId, content, rating, ... }
     * @param principal        로그인 사용자 정보
     * @return 201 Created + Location 헤더 + 생성된 ReviewResponseDto
     */
    @PostMapping
    public ResponseEntity<ReviewResponseDto> createReview(
            @RequestBody ReviewRequestDto reviewRequestDto,
            Principal principal) {

        String userName = principal.getName();
        // 서비스에서 ReviewResponseDto를 반환하도록 구현해야 함
        ReviewResponseDto created = reviewService.createReview(userName, reviewRequestDto);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()   // "/reviews"
                .path("/{id}")
                .buildAndExpand(created.getId())
                .toUri();

        return ResponseEntity.created(location).body(created);
    }

    /**
     * 리뷰 목록 조회 (페이징·정렬·필터링)
     * GET /reviews
     *
     * @param genre   (선택) 장르 필터: 예: "Action"
     * @param minRating (선택) 평점 최소 필터: 예: 4 (4 이상)
     * @param sortBy  (선택) 정렬 기준: "rating" 또는 "createdAt". 없으면 default: 최신순(createdAt desc)
     * @param pageable Spring Data Pageable: page, size 등의 페이징 정보. 정렬도 pageable로 받을 수 있음.
     *
     * 예시 요청:
     *  GET /reviews?genre=Action&minRating=4&sortBy=rating&page=0&size=10
     */
    @GetMapping
    public ResponseEntity<Page<ReviewResponseDto>> listReviews(
            @RequestParam(value = "genre", required = false) String genre,
            @RequestParam(value = "minRating", required = false) Integer minRating,
            @RequestParam(value = "sortBy", required = false) String sortBy,
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {

        // 서비스 메서드에 filter/정렬 정보를 전달
        // 예: reviewService.findReviews(genre, minRating, sortBy, pageable)
        Page<ReviewResponseDto> page = reviewService.findReviews(genre, minRating, sortBy, pageable);
        return ResponseEntity.ok(page);
    }

    /**
     * 특정 리뷰 조회
     * GET /reviews/{reviewId}
     */
    @GetMapping("/{reviewId}")
    public ResponseEntity<ReviewResponseDto> getReview(
            @PathVariable Long reviewId) {

        ReviewResponseDto dto = reviewService.getReviewById(reviewId);
        return ResponseEntity.ok(dto);
    }


}
