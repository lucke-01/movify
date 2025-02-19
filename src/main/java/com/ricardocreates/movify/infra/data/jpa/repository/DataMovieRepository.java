package com.ricardocreates.movify.infra.data.jpa.repository;

import com.ricardocreates.movify.infra.data.jpa.model.MovieEntity;
import jakarta.annotation.Nonnull;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DataMovieRepository extends PagingAndSortingRepository<MovieEntity, Long> {
    //@Override
    @EntityGraph(attributePaths = {"genres"})
    @Nonnull
    Optional<MovieEntity> findById(@Nonnull Long id);

    /*
        NOTE:
        Here the most optimize solution is use native query
        we use temporal table to get 50 top movies
        later based on this new top 50 movies, we use pagination with limit and offset.
        We do not need a count query since this would be always 50
        Improvements:
            We could make 50 a parameter?
            Does it make sense to paginate a 50 result? I do not think so,
                    better paginate search by title if you ask me.
     */
    @Query(value = """
            WITH TOP_50_MOVIES AS (
                SELECT * FROM MOVIFY.MOVIES ORDER BY rating DESC LIMIT 50
            )
            SELECT * FROM TOP_50_MOVIES LIMIT :limit offset :offset
            """,
            nativeQuery = true)
    List<MovieEntity> findTop50MoviesPaginated(int limit, long offset);


    List<MovieEntity> findByTitleContaining(String title, Sort sort);
}