package com.smile.review.controller;

import com.smile.review.dto.requestdto.CommentRequestDto;
import com.smile.review.dto.responsedto.CommentResponseDto;
import com.smile.review.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/reviews/{reviewId}/comments")
public class CommentController {

    private final CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    /**
     * 댓글 목록 조회
     * GET /reviews/{reviewId}/comments
     */
    @GetMapping
    public ResponseEntity<List<CommentResponseDto>> listComments(
            @PathVariable Long reviewId) {
        List<CommentResponseDto> list = commentService.listComments(reviewId);
        return ResponseEntity.ok(list);
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
