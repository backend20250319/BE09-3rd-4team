package com.smile.recommendservice.dto;

import lombok.Data;
// 별점 정보 전송용 DTO
/***
 * star-rating-service에서 별점 정보 받아올 때 사용할 수 있는 DTO
 */
@Data
public class StarRatingDto {
    private Long movieId;
    private Long userId;
    private Double star;
}

