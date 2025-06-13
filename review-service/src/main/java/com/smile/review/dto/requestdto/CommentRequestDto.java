package com.smile.review.dto.requestdto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class CommentRequestDto {
    private Long reviewId;
    private String content;
}
