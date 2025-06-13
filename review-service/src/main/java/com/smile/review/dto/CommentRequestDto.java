package com.smile.review.dto;

public class CommentRequestDto {
    private String content;

    public String getContent() {
        return content;
    }

    @Override
    public String toString() {
        return "CommentRequestDto{" +
                "content='" + content + '\'' +
                '}';
    }
}
