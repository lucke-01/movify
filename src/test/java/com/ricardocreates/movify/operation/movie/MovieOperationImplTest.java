package com.ricardocreates.movify.operation.movie;

import com.ricardocreates.movify.application.operation.movie.MovieOperationImpl;
import com.ricardocreates.movify.domain.entity.MovieDetail;
import com.ricardocreates.movify.domain.entity.MovieInfo;
import com.ricardocreates.movify.domain.exception.MovifyNotFoundException;
import com.ricardocreates.movify.domain.repository.MovieRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class MovieOperationImplTest {
    @Mock
    private MovieRepository movieRepository;
    @InjectMocks
    private MovieOperationImpl movieOperationImpl;


    @Test
    void should_findMovieById() {
        // GIVEN
        Long movieId = 1L;
        MovieDetail movieDetail = new MovieDetail();
        movieDetail.setId(movieId);
        given(movieRepository.findById(any(Long.class))).willReturn(movieDetail);

        // WHEN
        MovieDetail result = movieOperationImpl.findMovieById(movieId);

        // THEN
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(movieId);
        verify(movieRepository).findById(any());
    }

    @Test
    void should_not_findMovieById() {
        // GIVEN
        Long movieId = 1L;
        MovieDetail movieDetail = new MovieDetail();
        movieDetail.setId(movieId);
        given(movieRepository.findById(any(Long.class))).willThrow(new MovifyNotFoundException(""));

        // WHEN
        assertThatThrownBy(() -> movieOperationImpl.findMovieById(movieId))
                .isInstanceOf(MovifyNotFoundException.class);

        // THEN
        verify(movieRepository).findById(any());
    }

    @Test
    void should_findTop50MoviesByRating() {
        // GIVEN
        int page = 0;
        int size = 5;
        Page<MovieInfo> pageMovieInfo = mockPageMovieInfo(page, size);
        given(movieRepository.findTop50ByRating(any(), any())).willReturn(pageMovieInfo);

        // WHEN
        Page<MovieInfo> result = movieOperationImpl.findTop50MoviesByRating(page, size);

        // THEN
        assertThat(result).isNotNull();
        assertThat(result.getTotalElements()).isEqualTo(5);
        verify(movieRepository).findTop50ByRating(any(), any());
    }

    @Test
    void should_searchMovies() {
        // GIVEN
        String title = "the";
        List<MovieInfo> pageMovieInfo = mockListMovieInfo(title);
        given(movieRepository.searchMoviesByTitle(any(), any(), any())).willReturn(pageMovieInfo);

        // WHEN
        List<MovieInfo> result = movieOperationImpl.searchMovies(title, null, null);

        // THEN
        assertThat(result).isNotNull()
                .isNotEmpty();
        verify(movieRepository).searchMoviesByTitle(any(), any(), any());
    }

    private List<MovieInfo> mockListMovieInfo(String title) {
        final List<MovieInfo> movieInfoList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            MovieInfo movieInfo = mockMovieInfo(i);
            movieInfo.setTitle(title);
            movieInfoList.add(movieInfo);
        }
        return movieInfoList;
    }

    private Page<MovieInfo> mockPageMovieInfo(int page, int number) {
        final List<MovieInfo> movieInfoList = new ArrayList<>();
        for (int i = 0; i < number; i++) {
            MovieInfo movieInfo = mockMovieInfo(i);
            movieInfoList.add(movieInfo);
        }
        Pageable pageable = PageRequest.of(page, number);
        return new PageImpl<>(movieInfoList, pageable, movieInfoList.size());
    }

    public MovieInfo mockMovieInfo(Integer id) {
        final MovieInfo movie = new MovieInfo();
        movie.setId(id);
        movie.setTitle("Inception");
        movie.setReleaseDate(LocalDate.of(2010, 7, 16));
        movie.setAverageRate(new BigDecimal("8.8"));
        return movie;
    }
}
