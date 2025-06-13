package com.smile.review.dto;

import java.time.LocalDateTime;

public class CommentDto {

    private Long id;
    private Long reviewId;
    private Long userId;
    private String userName;
    private String content;
    private LocalDateTime createdAt;

    public Long getId() {
        return id;
    }

    public Long getReviewId() {
        return reviewId;
    }

    public Long getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public String getContent() {
        return content;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    @Override
    public String toString() {
        return "CommentDto{" +
                "id=" + id +
                ", reviewId=" + reviewId +
                ", userId=" + userId +
                ", userName='" + userName + '\'' +
                ", content='" + content + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
