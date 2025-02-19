package com.ricardocreates.movify.infra.rest.controller.mapper;

import com.ricardocreates.movify.domain.entity.MovieDetail;
import com.ricardocreates.movify.domain.entity.MovieInfo;
import com.swagger.client.codegen.rest.model.MovieDetailDTO;
import com.swagger.client.codegen.rest.model.MovieInfoDTO;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MovieDtoMapper {

    MovieInfo movieInfoToDomain(MovieInfoDTO movieInfoDto);

    MovieInfoDTO movieInfoFromDomain(MovieInfo movieInfo);

    List<MovieInfoDTO> movieInfoListFromDomain(List<MovieInfo> movieInfo);

    MovieDetail movieDetailToDomain(MovieDetailDTO couponDto);

    MovieDetailDTO movieDetailFromDomain(MovieDetail movieDetail);
}
