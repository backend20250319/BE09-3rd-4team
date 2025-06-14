package com.smile.searchservice.service;

import com.smile.searchservice.dto.SearchRequest;
import com.smile.searchservice.dto.SearchResponse;
import com.smile.searchservice.entity.Movie;
import com.smile.searchservice.repository.SearchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SearchService {

    private final SearchRepository searchRepository;

    // 키워드 검색
    public List<SearchResponse> searchMoviesByKeyword(String keyword) {
        String lowerKeyword = keyword.toLowerCase();
        return searchRepository.findAll().stream()
                .filter(search -> search.getTitle().toLowerCase().contains(lowerKeyword)
                        // Movie 엔티티의 genre, director, actor 필드가 객체이고 getName() 메서드를 가진다고 가정
                        || (search.getGenre() != null && search.getGenre().getName().toLowerCase().contains(lowerKeyword))
                        || (search.getDirector() != null && search.getDirector().getName().toLowerCase().contains(lowerKeyword))
                        || (search.getActor() != null && search.getActor().getName().toLowerCase().contains(lowerKeyword)))
                .map(SearchResponse::from)
                .collect(Collectors.toList());
    }

    // 장르별 평점순
    public List<SearchResponse> getMoviesByGenreAndRatingDesc(String genre) {
        // SearchRepository의 findByGenreOrderByRatingDesc 메서드를 findByGenre_NameOrderByRatingDesc로 변경 가정
        return searchRepository.findByGenreNameOrderByRatingDesc(genre).stream()
                .map(SearchResponse::from)
                .collect(Collectors.toList());
    }

    // 전체 평점순
    public List<SearchResponse> getAllMoviesByRatingDesc() {
        return searchRepository.findAllByOrderByRatingDesc().stream()
                .map(SearchResponse::from)
                .collect(Collectors.toList());
    }

    // 최신순 정렬
    public List<SearchResponse> getAllMoviesByReleaseDateDesc() {
        return searchRepository.findAllByOrderByReleaseDateDesc().stream()
                .map(SearchResponse::from)
                .collect(Collectors.toList());
    }

    // 전체 목록
    public List<SearchResponse> getAllMovies() {
        return searchRepository.findAll().stream()
                .map(SearchResponse::from)
                .collect(Collectors.toList());
    }

    // 단건 조회
    public SearchResponse getMovieById(Long id) {
        return searchRepository.findById(id)
                .map(SearchResponse::from)
                .orElseThrow(() -> new IllegalArgumentException("해당 영화가 없습니다. id = " + id));
    }

    // 영화 등록
    public SearchResponse createMovie(SearchRequest dto) {
        Movie movie = Movie.builder()
                .title(dto.getTitle())
                .genre(dto.getGenre())
                .director(dto.getDirector())
                .actor(dto.getActor())
                .description(dto.getDescription())
                .rating(dto.getRating())
                .releaseDate(dto.getReleaseDate())
                .build();

        return SearchResponse.from(searchRepository.save(movie));
    }

    // 영화 수정
    public SearchResponse updateMovie(Long id, SearchRequest dto) {
        Movie movie = searchRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 영화가 없습니다. id = " + id));

        movie.setTitle(dto.getTitle());
        movie.setGenre(dto.getGenre());
        movie.setDirector(dto.getDirector());
        movie.setActor(dto.getActor());
        movie.setDescription(dto.getDescription());
        movie.setRating(dto.getRating());
        movie.setReleaseDate(dto.getReleaseDate());

        return SearchResponse.from(searchRepository.save(movie));
    }

    // 영화 삭제
    public void deleteMovie(Long id) {
        searchRepository.deleteById(id);
    }

    // 배우별 영화 검색
    // SearchRepository의 findByActor 메서드를 findByActor_Name로 변경 가정
    public List<SearchResponse> getMoviesByActor(String actorName) {
        return searchRepository.findByActor(actorName).stream() // 메서드 이름 변경
                .map(SearchResponse::from)
                .collect(Collectors.toList());
    }

    // 감독별 영화 검색
    // 이 부분도 Movie 엔티티의 director 필드가 Director 객체라면 findByDirector_Name으로 변경해야 할 수 있습니다.
    public List<SearchResponse> getMoviesByDirector(String directorName) {
        return searchRepository.findByDirector(directorName).stream()
                .map(SearchResponse::from)
                .collect(Collectors.toList());
    }

    // 제목으로 영화 검색
    public List<SearchResponse> getMoviesByTitle(String title) {
        return searchRepository.findByTitle(title).stream()
                .map(SearchResponse::from)
                .collect(Collectors.toList());
    }

    // 장르별 영화 검색
    // 이 부분도 Movie 엔티티의 genre 필드가 Genre 객체라면 findByGenre_Name으로 변경해야 할 수 있습니다.
    public List<SearchResponse> getMoviesByGenre(String genre) {
        return searchRepository.findByGenre(genre).stream()
                .map(SearchResponse::from)
                .collect(Collectors.toList());
    }
}