package com.smile.review.controller;

import com.smile.review.dto.CommentDto;
import com.smile.review.dto.CommentRequestDto;
import com.smile.review.dto.ReviewResponseDto;
import com.smile.review.service.CommentService;
import com.smile.review.service.LikeService;
import com.smile.review.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
@RequestMapping("/reviews")
public class ReviewController {

    private final ReviewService reviewService;
    private final CommentService commentService;
    private final LikeService likeService;


    @Autowired
    public ReviewController(ReviewService reviewService, CommentService commentService, LikeService likeService) {
        this.reviewService = reviewService;
        this.commentService = commentService;
        this.likeService = likeService;
    }


    @PostMapping("/")
    public ResponseEntity<ReviewResponseDto> createReview() {

    }

    @GetMapping
    public

    @PostMapping("{reviewId}/comments")
    public ResponseEntity<CommentDto> addComment(@PathVariable Long reviewId, @RequestBody CommentRequestDto dto, Principal principal) {



    }

    @PostMapping("{reviewId}/like")
    public void writeLike(){

    }

    @GetMapping("")


}
