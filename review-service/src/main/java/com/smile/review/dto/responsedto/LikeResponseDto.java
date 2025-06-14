package com.smile.review.dto.responsedto;

import lombok.*;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class LikeResponseDto {
    private boolean liked;
    private long totalLikes;
}
