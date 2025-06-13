package com.smile.searchservice.controller;

import com.smile.searchservice.common.ApiResponse; // ApiResponse import는 유지
import com.smile.searchservice.dto.SearchRequest;
import com.smile.searchservice.dto.SearchResponse; // SearchResponse import는 유지
import com.smile.searchservice.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/*
 * 엔드포인트를 정의하는 클래스
 * 모든 URI 앞에 /api/v1/search/movies (주석에 따라 수정)
 */

@RestController
@RequestMapping("/search/movies") // 요청 경로
@RequiredArgsConstructor
public class SearchController {

    private final SearchService searchService;

    // 키워드 검색 (제목, 장르, 감독, 배우 포함)
    // GET /search/movies/keyword/기생충
    @GetMapping("/keyword/{keyword}")
    public ApiResponse<List<SearchResponse>> searchMoviesByKeyword(@PathVariable String keyword) {
        return ApiResponse.success(searchService.searchMoviesByKeyword(keyword));
    }

    // 장르별 평점순 정렬
    // GET /search/movies/genre/액션/rating
    @GetMapping("/genre/{genre}/rating")
    public ApiResponse<List<SearchResponse>> getMoviesByGenreAndRatingDesc(@PathVariable String genre) {
        return ApiResponse.success(searchService.getMoviesByGenreAndRatingDesc(genre));
    }

    // 전체 평점순 정렬
    // GET /search/movies/rating/all
    @GetMapping("/rating/all")
    public ApiResponse<List<SearchResponse>> getAllMoviesByRatingDesc() {
        return ApiResponse.success(searchService.getAllMoviesByRatingDesc());
    }

    // 최신순 정렬
    // GET /search/movies/latest
    @GetMapping("/latest")
    public ApiResponse<List<SearchResponse>> getAllMoviesByReleaseDateDesc() {
        return ApiResponse.success(searchService.getAllMoviesByReleaseDateDesc());
    }

    // 전체 영화 목록
    // GET /search/movies/all
    @GetMapping("/all")
    public ApiResponse<List<SearchResponse>> getAllMovies() {
        return ApiResponse.success(searchService.getAllMovies());
    }

    // 단건 조회
    // GET /search/movies/id/3
    @GetMapping("/id/{id}")
    public ApiResponse<SearchResponse> getMovieById(@PathVariable Long id) {
        return ApiResponse.success(searchService.getMovieById(id));
    }

    // 영화 등록
    // POST /search/movies
    @PostMapping
    public ApiResponse<SearchResponse> createMovie(@RequestBody SearchRequest request) {
        return ApiResponse.success(searchService.createMovie(request));
    }

    // 영화 수정
    // PUT /search/movies/{id}
    @PutMapping("/{id}")
    public ApiResponse<SearchResponse> updateMovie(@PathVariable Long id, @RequestBody SearchRequest request) {
        return ApiResponse.success(searchService.updateMovie(id, request));
    }

    // 영화 삭제
    // DELETE /search/movies/{id}
    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteMovie(@PathVariable Long id) {
        searchService.deleteMovie(id);
        return ApiResponse.success(null);
    }

    // 배우별 영화 검색
    // 반환 타입을 MovieDTO -> SearchResponse로 변경
    @GetMapping("/actor/{actorName}")
    public ApiResponse<List<SearchResponse>> getMoviesByActor(@PathVariable String actorName) {
        return ApiResponse.success(searchService.getMoviesByActor(actorName));
    }

    // 감독별 영화 검색
    // 반환 타입을 MovieDTO -> SearchResponse로 변경
    @GetMapping("/director/{directorName}")
    public ApiResponse<List<SearchResponse>> getMoviesByDirector(@PathVariable String directorName) {
        return ApiResponse.success(searchService.getMoviesByDirector(directorName));
    }

    // 제목으로 영화 검색
    // 반환 타입을 MovieDTO -> SearchResponse로 변경
    @GetMapping("/title/{title}")
    public ApiResponse<List<SearchResponse>> getMoviesByTitle(@PathVariable String title) {
        return ApiResponse.success(searchService.getMoviesByTitle(title));
    }

    // 장르별로 검색
    // 반환 타입을 MovieDTO -> SearchResponse로 변경 (주의: 위에 /genre/{genre}/rating과 경로가 겹칠 수 있으니 테스트 필요)
    @GetMapping("/genre/{genre}")
    public ApiResponse<List<SearchResponse>> getMoviesByGenre(@PathVariable String genre) {
        return ApiResponse.success(searchService.getMoviesByGenre(genre));
    }
}
