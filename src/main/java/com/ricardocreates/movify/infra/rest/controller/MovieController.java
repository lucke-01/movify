package com.ricardocreates.movify.infra.rest.controller;

import com.ricardocreates.movify.domain.operation.MovieOperation;
import com.ricardocreates.movify.infra.rest.controller.mapper.MovieDtoMapper;
import com.swagger.client.codegen.rest.MoviesApi;
import com.swagger.client.codegen.rest.model.MovieDetailDTO;
import com.swagger.client.codegen.rest.model.MovieInfoDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
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
    public ResponseEntity<List<MovieInfoDTO>> getPopularMovies(String apiKey) {
        return ResponseEntity.ok(
                movieDtoMapper.movieInfoListFromDomain(movieOperation.findTop50MoviesByRating())
        );
    }

    @Override
    public ResponseEntity<List<MovieInfoDTO>> searchMovies(String apiKey, String title,
                                                           String orderBy, String orderType) {
        return ResponseEntity.ok(
                movieDtoMapper.movieInfoListFromDomain(movieOperation.searchMovies(title, orderBy, orderType))
        );
    }
}
