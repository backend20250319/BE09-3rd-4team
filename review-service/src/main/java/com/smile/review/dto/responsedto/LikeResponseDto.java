package com.smile.review.dto.responsedto;

import lombok.*;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LikeResponseDto {
    private Long reviewId;
    private Long likeCount;
    private Boolean liked;
}
