package com.ricardocreates.movify.domain.repository;

import com.ricardocreates.movify.domain.entity.MovieDetail;
import com.ricardocreates.movify.domain.entity.MovieInfo;

import java.util.List;

public interface MovieRepository {
    MovieDetail findById(Long id);

    List<MovieInfo> findTop50ByRating();

    List<MovieInfo> searchMoviesByTitle(String title, String orderBy, String orderType);
}
