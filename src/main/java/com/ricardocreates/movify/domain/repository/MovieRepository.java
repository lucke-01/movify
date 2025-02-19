package com.ricardocreates.movify.domain.repository;

import com.ricardocreates.movify.domain.entity.MovieDetail;
import com.ricardocreates.movify.domain.entity.MovieInfo;
import com.ricardocreates.movify.domain.entity.OrderBy;
import com.ricardocreates.movify.domain.entity.OrderType;
import org.springframework.data.domain.Page;

import java.util.List;

public interface MovieRepository {
    MovieDetail findById(Long id);

    Page<MovieInfo> findTop50ByRating(Integer page, Integer size);

    List<MovieInfo> searchMoviesByTitle(String title, OrderBy orderBy, OrderType orderType);
}
