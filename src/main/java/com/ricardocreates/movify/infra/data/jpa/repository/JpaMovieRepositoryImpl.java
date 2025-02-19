package com.ricardocreates.movify.infra.data.jpa.repository;

import com.ricardocreates.movify.domain.entity.MovieDetail;
import com.ricardocreates.movify.domain.entity.MovieInfo;
import com.ricardocreates.movify.domain.repository.MovieRepository;
import com.ricardocreates.movify.infra.data.jpa.mapper.MovieJpaMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class JpaMovieRepositoryImpl implements MovieRepository {
    private final MovieJpaMapper movieJpaMapper;
    private final DataMovieRepository dataMovieRepository;

    @Override
    public MovieDetail findById(Long id) {
        return movieJpaMapper.toDomain(dataMovieRepository
                .findById(id)
                .orElseThrow(() -> new IllegalArgumentException("")));
    }

    @Override
    public Page<MovieInfo> findTop50ByRating(Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        return movieJpaMapper.convertToDtoPage(dataMovieRepository
                .findTop50ByOrderByRatingDesc(pageable)
        );
    }

    @Override
    public List<MovieInfo> searchMoviesByTitle(String title, String orderBy, String orderType) {
        Sort sort = Sort.unsorted();
        if (orderBy != null && !orderBy.isEmpty()) {
            Sort.Order order = "desc".equalsIgnoreCase(orderType)
                    ? Sort.Order.desc(orderBy)
                    : Sort.Order.asc(orderBy);
            sort = Sort.by(order);
        }
        return movieJpaMapper.movieInfoListToDomain(dataMovieRepository
                .findByTitleContaining(title, sort)
        );
    }
}