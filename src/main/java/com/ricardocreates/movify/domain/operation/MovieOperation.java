package com.ricardocreates.movify.domain.operation;

import com.ricardocreates.movify.domain.entity.MovieDetail;
import com.ricardocreates.movify.domain.entity.MovieInfo;
import org.springframework.data.domain.Page;

import java.util.List;

public interface MovieOperation {
    MovieDetail findMovieById(Long id);

    Page<MovieInfo> findTop50MoviesByRating(Integer page, Integer size);

    List<MovieInfo> searchMovies(String title, String orderBy, String orderType);
}
