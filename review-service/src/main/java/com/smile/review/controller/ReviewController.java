package com.smile.review.controller;


import com.smile.review.dto.requestdto.ReviewRequestDto;
import com.smile.review.dto.responsedto.ReviewResponseDto;
import com.smile.review.service.CommentService;
import com.smile.review.service.LikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


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
     * @param reviewRequestDto { movieId, content, rating }
     * @param principal        로그인 사용자 정보
     * @return 201 Created + Location 헤더 + 생성된 ReviewResponseDto
     */
    @PostMapping
    public ResponseEntity<ReviewResponseDto> createReview(@RequestBody ReviewRequestDto req
         , @AuthenticationPrincipal String username
    ) {
             System.out.println("Controller 진입 username: " + username);
        return ResponseEntity.ok(reviewService.createReview(
                username, req.getMovieId(), req.getContent(), req.getRating()
        ));

    }

    /**
     * 리뷰 목록 조회 (페이징·정렬·필터링)
     * GET /reviews?genre=action&rating=4&sort=rating
     *
     * @param genre   (선택) 장르 필터
     * @param rating  (선택) 평점 필터: 예: 4 → 평점이 4 이상 혹은 정확히 4 (서비스 사양에 맞춰)
     * @param sort    (선택) 정렬 기준: "rating" 또는 "createdAt". 없으면 default 최신순(createdAt desc)
     * @param pageable Spring Data Pageable: page, size, sort 파라미터가 있으면 반영
     */
    @GetMapping
    public ResponseEntity<Page<ReviewResponseDto>> listReviews(
            @RequestParam(value = "genre", required = false) String genre,
            @RequestParam(value = "rating", required = false) Integer rating,
            @RequestParam(value = "sort", required = false) String sort,
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {

        Page<ReviewResponseDto> page = reviewService.findReviews(genre, rating, sort, pageable);
        return ResponseEntity.ok(page);
    }

    /**
     * 특정 리뷰 조회
     * GET /reviews/{reviewId}
     */
    @GetMapping("/{reviewId}/{movieId}")
    public ResponseEntity<ReviewResponseDto> getReview(
            @PathVariable Long reviewId, @AuthenticationPrincipal String userId, Long movieId) {

        ReviewResponseDto dto = reviewService.getReviewId(reviewId,userId,movieId);
        return ResponseEntity.ok(dto);
    }


}
