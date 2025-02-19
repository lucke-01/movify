package com.ricardocreates.movify.infra.rest.controller;

import com.ricardocreates.movify.domain.entity.OrderBy;
import com.ricardocreates.movify.domain.entity.OrderType;
import com.ricardocreates.movify.domain.operation.MovieOperation;
import com.ricardocreates.movify.infra.rest.controller.mapper.MovieDtoMapper;
import com.swagger.client.codegen.rest.MoviesApi;
import com.swagger.client.codegen.rest.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api")
public class MovieController implements MoviesApi {
    private final MovieDtoMapper movieDtoMapper;
    private final MovieOperation movieOperation;

    @Override
    public ResponseEntity<MovieDetailDTO> getMovieDetail(String apiKey, Long id) {
        return ResponseEntity.ok(
                movieDtoMapper.movieDetailFromDomain(movieOperation.findMovieById(id))
        );
    }

    @Override
    public ResponseEntity<PageableMovieInfoDTO> getPopularMovies(String apiKey, Integer page, Integer size) {
        return ResponseEntity.ok(
                movieDtoMapper.pageMovieInfoToPageableMovieInfoDTO(movieOperation.findTop50MoviesByRating(page, size))
        );
    }

    @Override
    public ResponseEntity<List<MovieInfoDTO>> searchMovies(String apiKey, String title,
                                                           @Validated OrderByDTO orderBy, OrderTypeDTO orderType) {
        OrderBy orderByDomain = movieDtoMapper.orderByToDomain(orderBy);
        OrderType orderTypeDomain = movieDtoMapper.orderTypeToDomain(orderType);
        return ResponseEntity.ok(
                movieDtoMapper.movieInfoListFromDomain(
                        movieOperation.searchMovies(title, orderByDomain, orderTypeDomain)
                )
        );
    }
}
