
package com.smile.review.service.Impl;

import com.smile.review.client.MovieClient;
import com.smile.review.client.UserClient;
import com.smile.review.client.dto.MovieDto;
import com.smile.review.client.dto.UserDto;
import com.smile.review.common.ApiResponse;
import com.smile.review.domain.Review;

import com.smile.review.dto.requestdto.ReviewRequestDto;
import com.smile.review.dto.responsedto.ReviewResponseDto;


import com.smile.review.repository.review.ReviewRepository;
import com.smile.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserClient userClient;
    private final MovieClient movieClient;



    @Override
    @Transactional
    public ReviewResponseDto createReview(String userId, Long movieId, String content , double rating) {


        // 사용자 조회
        ApiResponse<UserDto> userDtoApiResponse = userClient.getUserId(userId);
        UserDto userDto = userDtoApiResponse.getData();
        try {
            userDto = userClient.getUserId(userId).getData();
        } catch (Exception ex) {
            throw new IllegalArgumentException("사용자 정보를 가져올 수 없습니다: " + userId, ex);
        }
        if (userDto == null || userDto.getUserId() == null) {
            throw new IllegalArgumentException("유효하지 않은 사용자: " + userId);
        }

        // 영화 조회/검증
        MovieDto movieDto;
        try {
            movieDto = movieClient.getMovieId(movieId).getData();
        } catch (Exception ex) {
            throw new IllegalArgumentException("영화 정보를 가져올 수 없습니다: movieId=" + movieId, ex);
        }
        if (movieDto == null || movieDto.getMovieId() == null) {
            throw new IllegalArgumentException("유효하지 않은 영화 ID: " + movieId);
        }

        // 엔티티 생성·저장
        Review review = new Review();
        review.setUserId(userId);
        review.setMovieId(movieId);
        review.setContent(content);
        review.setRating(rating);
        review.setCreatedAt(LocalDateTime.now());
        Review saved = reviewRepository.save(review);

        // DTO 변환
        return ReviewResponseDto.fromEntity(saved, userDto.getUserId(), movieDto.getTitle());
    }

    @Override
    @Transactional(readOnly = true)
    public ReviewResponseDto getReviewId(Long reviewId, String userId,Long movieId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 리뷰 ID: " + reviewId));

        // 작성자 정보 조회
        UserDto userDto;
        try {
            userDto = userClient.getUserId(userId).getData();
        } catch (Exception ex) {
            throw new IllegalArgumentException("사용자 정보를 가져올 수 없습니다: id=" + review.getUserId(), ex);
        }
        // 영화 정보 조회
        MovieDto movieDto;
        try {
            movieDto = movieClient.getMovieId(review.getMovieId()).getData();
        } catch (Exception ex) {
            throw new IllegalArgumentException("영화 정보를 가져올 수 없습니다: id=" + review.getMovieId(), ex);
        }

        return ReviewResponseDto.fromEntity(review, userDto.getUserName(), movieDto.getTitle());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ReviewResponseDto> listReviews(int page, int size, String sortBy) {
        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
        if ("rating".equalsIgnoreCase(sortBy)) {
            sort = Sort.by(Sort.Direction.DESC, "rating");
        }
        Pageable pageable = PageRequest.of(page, size, sort);

        return reviewRepository.findAll(pageable)
                .map(review -> {
                    UserDto userDto = userClient.getUserId(review.getUserId()).getData();
                    MovieDto movieDto = movieClient.getMovieId(review.getMovieId()).getData();
                    return ReviewResponseDto.fromEntity(
                            review,
                            userDto.getUserId(),
                            movieDto.getTitle()
                    );
                });
    }

    @Override
    @Transactional
    public Page<ReviewResponseDto> findReviews(String genre, Double rating, String sort, Pageable pageable) {
        return reviewRepository.findAll(pageable)
                .map(review -> {
                    UserDto userDto = userClient.getUserId(review.getUserId()).getData();
                    MovieDto movieDto = movieClient.getMovieId(review.getMovieId()).getData();
                    return ReviewResponseDto.fromEntity(
                            review,
                            userDto.getUserId(),
                            movieDto.getTitle()
                    );
                });
    }


    @Override
    @Transactional
    public ReviewResponseDto editReview(Long reviewId, String userName, ReviewRequestDto dto) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 리뷰 ID: " + reviewId));

        // 소유권 확인
        UserDto userDto;
        try {
            userDto = userClient.getUserId(review.getUserId()).getData();
        } catch (Exception ex) {
            throw new IllegalArgumentException("사용자 정보를 가져올 수 없습니다: " + userName, ex);
        }
        if (userDto == null || userDto.getUserId() == null) {
            throw new IllegalArgumentException("유효하지 않은 사용자: " + userName);
        }
        if (!review.getUserId().equals(userDto.getUserId())) {
            throw new AccessDeniedException("본인이 작성한 리뷰만 수정할 수 있습니다.");
        }

        // 수정할 내용 반영 (movieId 변경을 허용하지 않는다면 movieId 체크 생략)
        review.setContent(dto.getContent());
        review.setRating(dto.getRating());
        review.setUpdatedAt(LocalDateTime.now());
        Review updated = reviewRepository.save(review);

        // 최신 사용자/영화 정보
        MovieDto movieDto;
        try {
            movieDto = movieClient.getMovieId(review.getMovieId()).getData();
        } catch (Exception ex) {
            throw new IllegalArgumentException("영화 정보를 가져올 수 없습니다: id=" + updated.getMovieId(), ex);
        }
        return ReviewResponseDto.fromEntity(updated, userDto.getUserId(), movieDto.getTitle());
    }

    @Override
    @Transactional
    public void deleteReview(Long reviewId, Long userId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 리뷰 ID: " + reviewId));

        // 소유권 확인
        UserDto userDto;
        try {
            userDto = userClient.getUserId(String.valueOf(userId)).getData();
        } catch (Exception ex) {
            throw new IllegalArgumentException("사용자 정보를 가져올 수 없습니다: " + userId, ex);
        }
        if (userDto == null || userDto.getUserId() == null) {
            throw new IllegalArgumentException("유효하지 않은 사용자: " + userId);
        }
        if (!review.getUserId().equals(userDto.getUserId())) {
            throw new AccessDeniedException("본인이 작성한 리뷰만 삭제할 수 있습니다.");
        }

        reviewRepository.delete(review);
    }



}
