package com.ricardocreates.movify.domain.operation;

import com.ricardocreates.movify.domain.entity.MovieDetail;
import com.ricardocreates.movify.domain.entity.MovieInfo;

import java.util.List;

public interface MovieOperation {
    MovieDetail findMovieById(Long id);

    List<MovieInfo> findTop50MoviesByRating();

    List<MovieInfo> searchMovies(String title, String orderBy, String orderType);
}
