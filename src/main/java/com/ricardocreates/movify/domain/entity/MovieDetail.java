package com.ricardocreates.movify.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MovieDetail {
    private Long id;

    private String title;

    private LocalDate releaseDate;

    private String fullPosterUrl;

    private String overview;

    private List<String> genres;

    private BigDecimal averageRate;

    private Integer runtime;

    private String language;
}
