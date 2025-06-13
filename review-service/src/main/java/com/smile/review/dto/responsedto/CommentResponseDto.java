package com.smile.review.dto.responsedto;

import com.smile.review.domain.Comment;
import lombok.*;
import java.time.LocalDateTime;

/**
 * 리뷰 댓글 응답용 DTO
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentResponseDto {
    private Long id;
    private Long reviewId;
    private Long userId;
    private String userName;  // user 서비스에서 받아온 사용자 이름
    private String content;
    private LocalDateTime createdAt;

    /**
     * Comment 엔티티 → DTO 변환
     *
     * @param comment Comment 엔티티
     * @param userDto 사용자 정보를 담은 객체 (Feign 호출 결과 등). 내부에서 캐스팅해 필요한 정보 추출.
     * @return CommentResponseDto
     */
    public static CommentResponseDtoBuilder fromEntity(Comment comment, Object userDto) {
        // userDto에서 사용자 이름을 추출하는 로직 예시:
        String userName = "";
        if (userDto != null) {
            // 실제 UserDto 타입이 있다면 아래처럼 캐스팅 후 getName() 호출
            // if (userDto instanceof UserDto) userName = ((UserDto) userDto).getName();
            // 예시: userDto가 Map 등이라면 Map에서 꺼내오기
        }
        return CommentResponseDto.builder();
    }
}
