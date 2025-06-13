package com.smile.review.service;

import com.smile.review.domain.Comment;
import com.smile.review.dto.requestdto.CommentRequestDto;
import com.smile.review.dto.responsedto.CommentResponseDto;
import com.smile.review.repository.ReviewRepository;
import com.smile.review.repository.comment.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final ReviewRepository reviewRepository;
    private final UserClient userClient;

    @Autowired
    public CommentService(CommentRepository commentRepository,
                          ReviewRepository reviewRepository,
                          UserClient userClient
    ) {
        this.commentRepository = commentRepository;
        this.reviewRepository = reviewRepository;
        this.userClient = userClient;
    }

    @Transactional
    public CommentResponseDto.CommentResponseDtoBuilder createComment(Long userId, CommentRequestDto dto) {
        if (!reviewRepository.existsById(dto.getReviewId())) {
            throw new IllegalArgumentException("해당 리뷰가 없습니다: " + dto.getReviewId());
        }
        Comment comment = Comment.builder()
                .reviewId(dto.getReviewId())
                .userId(userId)
                .content(dto.getContent())
                .createdAt(LocalDateTime.now())
                .build();
        Comment saved = commentRepository.save(comment);
        Object userDto = userClient.getUserById(userId);
        return CommentResponseDto.fromEntity(saved, userDto);
    }

    public Page<CommentResponseDto> getComments(Long reviewId, int page, int size) {
        if (!reviewRepository.existsById(reviewId)) {
            throw new IllegalArgumentException("해당 리뷰가 없습니다: " + reviewId);
        }
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        return commentRepository.findByReviewId(reviewId, pageable)
                .map(comment -> {
                    Object userDto = userClient.getUserId(comment.getUserId());
                    return CommentResponseDto.fromEntity(comment, userDto);
                });
    }

    @Override
    @Transactional
    public CommentResponseDto editComment(Long reviewId, Long commentId, String userName, CommentRequestDto dto) {
        // 1) review 존재 확인
        if (!reviewRepository.existsById(reviewId)) {
            throw new IllegalArgumentException("해당 리뷰가 없습니다: " + reviewId);
        }
        // 2) 댓글 조회
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("해당 댓글이 없습니다: " + commentId));
        // 3) 댓글이 해당 리뷰에 속하는지 확인
        if (!comment.getReviewId().equals(reviewId)) {
            throw new IllegalArgumentException("댓글이 해당 리뷰에 속하지 않습니다. reviewId=" + reviewId + ", commentId=" + commentId);
        }
        // 4) 사용자 정보 조회
        UserDto userDto;
        try {
            userDto = userClient.getUserByName(userName);
        } catch (Exception ex) {
            throw new IllegalArgumentException("사용자 정보를 가져올 수 없습니다: " + userName, ex);
        }
        if (userDto == null || userDto.getId() == null) {
            throw new IllegalArgumentException("유효하지 않은 사용자: " + userName);
        }
        Long userId = userDto.getId();
        // 5) 소유권 확인: 현재 사용자가 댓글 작성자와 동일한지
        if (!comment.getUserId().equals(userId)) {
            throw new AccessDeniedException("본인이 작성한 댓글만 수정할 수 있습니다.");
        }
        // 6) 수정 처리
        comment.setContent(dto.getContent());
        comment.setUpdatedAt(LocalDateTime.now());  // 엔티티에 updatedAt 필드가 있어야 함
        Comment updated = commentRepository.save(comment);

        return CommentResponseDto.fromEntity(updated, userDto);
    }

    @Override
    @Transactional
    public void deleteComment(Long reviewId, Long commentId, String userName) {
        // 1) review 존재 확인
        if (!reviewRepository.existsById(reviewId)) {
            throw new IllegalArgumentException("해당 리뷰가 없습니다: " + reviewId);
        }
        // 2) 댓글 조회
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("해당 댓글이 없습니다: " + commentId));
        // 3) 댓글이 해당 리뷰에 속하는지 확인
        if (!comment.getReviewId().equals(reviewId)) {
            throw new IllegalArgumentException("댓글이 해당 리뷰에 속하지 않습니다. reviewId=" + reviewId + ", commentId=" + commentId);
        }
        // 4) 사용자 정보 조회
        UserDto userDto;
        try {
            userDto = userClient.getUserByName(userName);
        } catch (Exception ex) {
            throw new IllegalArgumentException("사용자 정보를 가져올 수 없습니다: " + userName, ex);
        }
        if (userDto == null || userDto.getId() == null) {
            throw new IllegalArgumentException("유효하지 않은 사용자: " + userName);
        }
        Long userId = userDto.getId();
        // 5) 소유권 확인: 본인 댓글만 삭제 허용
        if (!comment.getUserId().equals(userId)) {
            throw new AccessDeniedException("본인이 작성한 댓글만 삭제할 수 있습니다.");
        }
        // 6) 삭제
        commentRepository.delete(comment);
    }
}
