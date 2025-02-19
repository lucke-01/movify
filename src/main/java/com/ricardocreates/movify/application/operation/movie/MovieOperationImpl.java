package com.ricardocreates.movify.application.operation.movie;

import com.ricardocreates.movify.domain.entity.MovieDetail;
import com.ricardocreates.movify.domain.entity.MovieInfo;
import com.ricardocreates.movify.domain.operation.MovieOperation;
import com.ricardocreates.movify.domain.repository.MovieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MovieOperationImpl implements MovieOperation {
    private final MovieRepository movieRepository;

    @Override
    public MovieDetail findMovieById(Long id) {
        return movieRepository.findById(id);
    }

    @Override
    public List<MovieInfo> findTop50MoviesByRating() {
        return movieRepository.findTop50ByRating();
    }

    @Override
    public List<MovieInfo> searchMovies(String title, String orderBy, String orderType) {
        return movieRepository.searchMoviesByTitle(title, orderBy, orderType);
    }
}
