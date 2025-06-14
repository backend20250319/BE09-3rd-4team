package com.smile.review.dto.requestdto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewRequestDto {
    private Long userId;
    private Long movieId;
    private String content;
    private Integer rating;
}
