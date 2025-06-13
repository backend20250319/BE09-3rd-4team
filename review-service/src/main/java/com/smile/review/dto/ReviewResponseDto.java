package com.smile.review.dto;

import java.time.LocalDateTime;
import java.util.List;

public class ReviewResponseDto {

    private Long id;
    private Long filmId;
    private String filmTitle;
    private  Long userId;
    private String userName;
    private  String content;
    private int rating;
    private LocalDateTime createdAt;
    private long likeCount;
    private List<CommentDto> comments;

    public Long getId() {
        return id;
    }

    public Long getFilmId() {
        return filmId;
    }

    public String getFilmTitle() {
        return filmTitle;
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

    public int getRating() {
        return rating;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public long getLikeCount() {
        return likeCount;
    }

    public List<CommentDto> getComments() {
        return comments;
    }

    @Override
    public String toString() {
        return "ReviewResponseDto{" +
                "id=" + id +
                ", filmId=" + filmId +
                ", filmTitle='" + filmTitle + '\'' +
                ", userId=" + userId +
                ", userName='" + userName + '\'' +
                ", content='" + content + '\'' +
                ", rating=" + rating +
                ", createdAt=" + createdAt +
                ", likeCount=" + likeCount +
                ", comments=" + comments +
                '}';
    }
}
