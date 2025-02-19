package com.ricardocreates.movify.infra.data.jpa.repository;

import com.ricardocreates.movify.infra.data.jpa.model.MovieEntity;
import jakarta.annotation.Nonnull;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DataMovieRepository extends JpaRepository<MovieEntity, Long> {
    @Override
    @EntityGraph(attributePaths = {"genres"})
    @Nonnull
    Optional<MovieEntity> findById(@Nonnull Long id);

    List<MovieEntity> findTop50ByOrderByRatingDesc();


    List<MovieEntity> findByTitleContaining(String title, Sort sort);
}