package com.smile.review.dto;

public class LikeResponseDto {

    private Long likeCount;
    private boolean likedByUser;

    public Long getLikeCount() {
        return likeCount;
    }

    public boolean isLikedByUser() {
        return likedByUser;
    }

    @Override
    public String toString() {
        return "LikeResponseDto{" +
                "likeCount=" + likeCount +
                ", likedByUser=" + likedByUser +
                '}';
    }
}
