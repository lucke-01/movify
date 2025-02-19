package com.ricardocreates.movify.infra.rest.controller.mapper;

import com.ricardocreates.movify.domain.entity.MovieDetail;
import com.ricardocreates.movify.domain.entity.MovieInfo;
import com.swagger.client.codegen.rest.model.MovieDetailDTO;
import com.swagger.client.codegen.rest.model.MovieInfoDTO;
import com.swagger.client.codegen.rest.model.PageDTO;
import com.swagger.client.codegen.rest.model.PageableMovieInfoDTO;
import org.mapstruct.Mapper;
import org.springframework.data.domain.Page;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MovieDtoMapper {

    MovieInfo movieInfoToDomain(MovieInfoDTO movieInfoDto);

    MovieInfoDTO movieInfoFromDomain(MovieInfo movieInfo);

    List<MovieInfoDTO> movieInfoListFromDomain(List<MovieInfo> movieInfo);

    MovieDetail movieDetailToDomain(MovieDetailDTO couponDto);

    MovieDetailDTO movieDetailFromDomain(MovieDetail movieDetail);

    default PageDTO pageDtoFromDomain(Page<?> page) {
        final PageDTO pageDTO = new PageDTO();
        pageDTO.setTotalPages(page.getTotalPages());
        pageDTO.setTotalElements(page.getTotalElements());
        pageDTO.setSize(page.getSize());

        return pageDTO;
    }

    default PageableMovieInfoDTO pageMovieInfoToPageableMovieInfoDTO(Page<MovieInfo> pageMovieInfo) {
        final PageableMovieInfoDTO pageableMovieInfoDTO = new PageableMovieInfoDTO();
        final List<MovieInfoDTO> pageContentDto = movieInfoListFromDomain(pageMovieInfo.getContent());
        pageableMovieInfoDTO.setContent(pageContentDto);

        pageableMovieInfoDTO.setPage(pageDtoFromDomain(pageMovieInfo));

        return pageableMovieInfoDTO;
    }
}
