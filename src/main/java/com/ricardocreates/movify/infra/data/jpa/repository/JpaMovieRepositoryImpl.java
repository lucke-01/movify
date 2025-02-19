package com.ricardocreates.movify.infra.data.jpa.repository;

import com.ricardocreates.movify.domain.entity.MovieDetail;
import com.ricardocreates.movify.domain.entity.MovieInfo;
import com.ricardocreates.movify.domain.entity.OrderBy;
import com.ricardocreates.movify.domain.entity.OrderType;
import com.ricardocreates.movify.domain.exception.MovifyNotFoundException;
import com.ricardocreates.movify.domain.repository.MovieRepository;
import com.ricardocreates.movify.infra.data.jpa.mapper.MovieJpaMapper;
import jakarta.annotation.Nonnull;
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
    public MovieDetail findById(@Nonnull Long id) {
        return movieJpaMapper.toDomain(dataMovieRepository
                .findById(id)
                .orElseThrow(() -> new MovifyNotFoundException("Movie not found")));
    }

    @Override
    public Page<MovieInfo> findTop50ByRating(@Nonnull Integer page, @Nonnull Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        return movieJpaMapper.convertToDtoPage(dataMovieRepository
                .findTop50MoviesPaginated(pageable.getPageSize(), pageable.getOffset()), pageable
        );
    }

    @Override
    public List<MovieInfo> searchMoviesByTitle(@Nonnull String title, OrderBy orderBy, OrderType orderType) {
        Sort sort = Sort.unsorted();
        if (orderBy != null) {
            Sort.Order order = OrderType.DESC.equals(orderType)
                    ? Sort.Order.desc(orderBy.getValue())
                    : Sort.Order.asc(orderBy.getValue());
            sort = Sort.by(order);
        }
        return movieJpaMapper.movieInfoListToDomain(dataMovieRepository
                .findByTitleContaining(title, sort)
        );
    }
}