package com.smile.review.dto.responsedto;

import com.smile.review.domain.Review;
import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewResponseDto {
    private Long id;
    private Long userId;
    private String userName;      // user 서비스에서 가져온 정보
    private Long movieId;
    private String movieTitle;    // movie 서비스에서 가져온 정보
    private String content;
    private Integer rating;
    private LocalDateTime createdAt;
    private Long likeCount;

    public static ReviewResponseDtoBuilder fromEntity(
            Review review,
            /*UserDto userDto*/ Object userDto,
            /*MovieDto movieDto*/ Object movieDto,
            Long commentCount,
            Long likeCount) {
        // userDto, movieDto에서 필요한 정보 꺼내오는 로직 예시:
        String userName = "";
        String movieTitle = "";
        // 가정: userDto.getName(), movieDto.getTitle() 등
        // 실제 타입에 맞춰 캐스팅 후 호출
        // 예:
        // if (userDto instanceof UserDto) userName = ((UserDto)userDto).getName();
        // if (movieDto instanceof MovieDto) movieTitle = ((MovieDto)movieDto).getTitle();

        return ReviewResponseDto.builder();
    }
}
