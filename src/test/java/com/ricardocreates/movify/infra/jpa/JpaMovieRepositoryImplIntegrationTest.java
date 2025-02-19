package com.ricardocreates.movify.infra.jpa;

import com.ricardocreates.movify.domain.entity.MovieDetail;
import com.ricardocreates.movify.domain.entity.MovieInfo;
import com.ricardocreates.movify.domain.entity.OrderBy;
import com.ricardocreates.movify.domain.entity.OrderType;
import com.ricardocreates.movify.domain.exception.MovifyNotFoundException;
import com.ricardocreates.movify.infra.data.jpa.repository.JpaMovieRepositoryImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@ActiveProfiles({"test"})
public class JpaMovieRepositoryImplIntegrationTest {
    private static final long TOTAL_TOP = 50;
    @Autowired
    private JpaMovieRepositoryImpl jpaMovieRepositoryImpl;

    @Test
    void should_found_movie() {
        //GIVEN
        Long id = 1L;
        //WHEN
        MovieDetail movieDetail = jpaMovieRepositoryImpl.findById(id);
        //THEN
        assertThat(movieDetail).isNotNull();
        assertThat(movieDetail.getId()).isEqualTo(id);
    }

    @Test
    void should_not_found_movie() {
        //GIVEN
        Long id = -1L;
        //WHEN
        assertThatThrownBy(() -> jpaMovieRepositoryImpl.findById(id))
                .isInstanceOf(MovifyNotFoundException.class);
        //THEN
    }

    @Test
    void should_findTop50ByRating() {
        //GIVEN
        Integer page = 0;
        Integer size = 10;
        //WHEN
        Page<MovieInfo> pageMovieInfo = jpaMovieRepositoryImpl.findTop50ByRating(page, size);
        //THEN
        var movies = pageMovieInfo.getContent();
        assertThat(pageMovieInfo).isNotNull();
        assertThat(pageMovieInfo.getSize()).isEqualTo(size);
        assertThat(pageMovieInfo.getTotalElements()).isEqualTo(TOTAL_TOP);
        assertThat(movies).isNotNull().hasSize(size);
        assertThat(movies.getFirst().getId()).isNotNull();
        assertThat(movies.getLast().getId()).isNotNull();
    }

    @Test
    void should_not_findTop50ByRating_page_empty() {
        //GIVEN
        Integer page = 100;
        Integer size = 10;
        //WHEN
        Page<MovieInfo> pageMovieInfo = jpaMovieRepositoryImpl.findTop50ByRating(page, size);
        //THEN
        var movies = pageMovieInfo.getContent();
        assertThat(pageMovieInfo).isNotNull();
        assertThat(pageMovieInfo.getSize()).isEqualTo(size);
        assertThat(pageMovieInfo.getTotalElements()).isEqualTo(TOTAL_TOP);
        assertThat(movies).isEmpty();
    }

    @Test
    void should_searchMoviesByTitle() {
        //GIVEN
        String title = "the";
        //WHEN
        List<MovieInfo> movies = jpaMovieRepositoryImpl
                .searchMoviesByTitle(title, null, null);
        //THEN
        assertThat(movies).isNotNull();
        assertThat(movies).hasSize(19);
        assertThat(movies.getFirst().getId()).isNotNull();
        assertThat(movies.getLast().getId()).isNotNull();
    }

    @Test
    void should_searchMoviesByTitle_order_asc() {
        //GIVEN
        String title = "the";
        String first = "Action Rather";
        String last = "Thriller Mother";
        //WHEN
        List<MovieInfo> movies = jpaMovieRepositoryImpl
                .searchMoviesByTitle(title, OrderBy.TITLE, null);
        //THEN
        assertThat(movies).isNotNull();
        assertThat(movies).hasSize(19);
        assertThat(movies.getFirst().getId()).isNotNull();
        assertThat(movies.getLast().getId()).isNotNull();
        assertThat(movies.getFirst().getTitle()).isEqualTo(first);
        assertThat(movies.getLast().getTitle()).isEqualTo(last);
    }

    @Test
    void should_searchMoviesByTitle_order_desc() {
        //GIVEN
        String title = "the";
        String first = "Thriller Mother";
        String last = "Action Rather";
        //WHEN
        List<MovieInfo> movies = jpaMovieRepositoryImpl
                .searchMoviesByTitle(title, OrderBy.TITLE, OrderType.DESC);
        //THEN
        assertThat(movies).isNotNull();
        assertThat(movies).hasSize(19);
        assertThat(movies.getFirst().getId()).isNotNull();
        assertThat(movies.getLast().getId()).isNotNull();
        assertThat(movies.getFirst().getTitle()).isEqualTo(first);
        assertThat(movies.getLast().getTitle()).isEqualTo(last);
    }

    @Test
    void should_not_searchMoviesByTitle_no_result() {
        //GIVEN
        String title = "NORESULTATALL";
        //WHEN
        List<MovieInfo> movies = jpaMovieRepositoryImpl
                .searchMoviesByTitle(title, null, null);
        //THEN
        assertThat(movies).isNotNull();
        assertThat(movies).isEmpty();
    }
}
