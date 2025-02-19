package com.ricardocreates.movify.domain.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
public class MovieDetail {
    private Integer id;

    private String title;

    private LocalDate releaseDate;

    private String fullPosterUrl;

    private String overview;

    private List<String> genres;

    private BigDecimal averageRate;

    private Integer runtime;

    private String language;
}
