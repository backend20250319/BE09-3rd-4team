package com.smile.movieservice.service;

import com.smile.movieservice.dto.MovieRequest;
import com.smile.movieservice.dto.MovieResponse;
import com.smile.movieservice.entity.Movie;
import com.smile.movieservice.repository.MovieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MovieService {

    private final MovieRepository movieRepository;

    // 영화 등록
    public MovieResponse createMovie(MovieRequest request) {
        Movie movie = Movie.builder()
                .title(request.getTitle())
                .overview(request.getOverview())
                .genre(request.getGenre())
                .rating(request.getRating())
                .releaseDate(request.getReleaseDate())
                .posterUrl(request.getPosterUrl())
                .build();

        Movie saved = movieRepository.save(movie);

        return MovieResponse.builder()
                .id(saved.getId())
                .title(saved.getTitle())
                .overview(saved.getOverview())
                .genre(saved.getGenre())
                .rating(saved.getRating())
                .releaseDate(saved.getReleaseDate())
                .posterUrl(saved.getPosterUrl())
                .build();
    }
    // 영화 수정
    public MovieResponse updateMovie(Long id, MovieRequest request) {
        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("해당 ID의 영화가 존재하지 않습니다."));

        movie.setTitle(request.getTitle());
        movie.setOverview(request.getOverview());
        movie.setGenre(request.getGenre());
        movie.setRating(request.getRating());
        movie.setReleaseDate(request.getReleaseDate());
        movie.setPosterUrl(request.getPosterUrl());

        Movie updated = movieRepository.save(movie);

        return MovieResponse.builder()
                .id(updated.getId())
                .title(updated.getTitle())
                .overview(updated.getOverview())
                .genre(updated.getGenre())
                .rating(updated.getRating())
                .releaseDate(updated.getReleaseDate())
                .posterUrl(updated.getPosterUrl())
                .build();
    }
    // 영화 삭제
    public void deleteMovie(Long id) {
        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("해당 ID의 영화가 존재하지 않습니다."));

        movieRepository.delete(movie);
    }

    // 영화 전체 조회
    public List<MovieResponse> getAllMovies() {
        List<Movie> movies = movieRepository.findAll();

        return movies.stream()
                .map(movie -> MovieResponse.builder()
                        .id(movie.getId())
                        .title(movie.getTitle())
                        .overview(movie.getOverview())
                        .genre(movie.getGenre())
                        .rating(movie.getRating())
                        .releaseDate(movie.getReleaseDate())
                        .posterUrl(movie.getPosterUrl())
                        .build())
                .toList();
    }

    // 영화 호출 조회
    public MovieResponse getMovieById(Long id) {
        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("해당 ID의 영화가 존재하지 않습니다."));

        return MovieResponse.builder()
                .id(movie.getId())
                .title(movie.getTitle())
                .overview(movie.getOverview())
                .genre(movie.getGenre())
                .rating(movie.getRating())
                .releaseDate(movie.getReleaseDate())
                .posterUrl(movie.getPosterUrl())
                .build();
    }

}