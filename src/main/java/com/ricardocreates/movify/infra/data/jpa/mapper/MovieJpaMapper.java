package com.ricardocreates.movify.infra.data.jpa.mapper;

import com.ricardocreates.movify.domain.entity.MovieDetail;
import com.ricardocreates.movify.domain.entity.MovieInfo;
import com.ricardocreates.movify.infra.data.jpa.model.GenreEntity;
import com.ricardocreates.movify.infra.data.jpa.model.MovieEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring")
public interface MovieJpaMapper {
    default List<String> genresListMapName(Set<GenreEntity> genres) {
        return genres.stream().map(GenreEntity::getName).toList();
    }

    @Mapping(target = "fullPosterUrl", source = "posterUrl")
    @Mapping(target = "averageRate", source = "rating")
    MovieDetail toDomain(MovieEntity movieEntity);
    
    @Mapping(target = "averageRate", source = "rating")
    MovieInfo movieInfoToDomain(MovieEntity movieEntity);

    List<MovieInfo> movieInfoListToDomain(List<MovieEntity> movieEntity);
}
