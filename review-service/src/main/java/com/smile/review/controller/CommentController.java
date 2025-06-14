package com.smile.review.controller;

import com.smile.review.dto.requestdto.CommentRequestDto;
import com.smile.review.dto.responsedto.CommentResponseDto;
import com.smile.review.service.CommentService;
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


@RestController
@RequestMapping("/reviews/{reviewId}/comments")
public class CommentController {

    private final CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    /** 댓글 목록 조회 (예시) */
    @GetMapping
    public ResponseEntity<Page<CommentResponseDto>> listComments(
            @PathVariable Long reviewId,
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<CommentResponseDto> page = commentService.getComments(reviewId, pageable.getPageNumber(), pageable.getSize());
        return ResponseEntity.ok(page);
    }

    /**
     * 댓글 작성
     * POST /reviews/{reviewId}/comments
     */
    @PostMapping
    public ResponseEntity<CommentResponseDto> addComment(
            @PathVariable Long reviewId,
            @RequestBody CommentRequestDto dto,
            Principal principal) {
        String userName = principal.getName();
        CommentResponseDto created = commentService.addComment(reviewId, userName, dto);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()   // "/reviews/{reviewId}/comments"
                .path("/{id}")
                .buildAndExpand(created.getId())
                .toUri();
        return ResponseEntity.created(location).body(created);
    }


    /**
     * 댓글 수정
     * PUT /reviews/{reviewId}/comments/{commentId}
     */
    @PutMapping("/{commentId}")
    public ResponseEntity<CommentResponseDto> editComment(
            @PathVariable Long reviewId,
            @PathVariable Long commentId,
            @RequestBody CommentRequestDto dto,
            Principal principal) {
        String userName = principal.getName();
        CommentResponseDto updated = commentService.editComment(reviewId, commentId, userName, dto);
        return ResponseEntity.ok(updated);
    }

    /**
     * 댓글 삭제
     * DELETE /reviews/{reviewId}/comments/{commentId}
     */
    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(
            @PathVariable Long reviewId,
            @PathVariable Long commentId,
            Principal principal) {
        String userName = principal.getName();
        commentService.deleteComment(reviewId, commentId, userName);
        return ResponseEntity.noContent().build();
    }
}
