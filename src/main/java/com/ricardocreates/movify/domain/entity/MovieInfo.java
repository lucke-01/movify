package com.ricardocreates.movify.domain.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class MovieInfo {
    private Integer id;

    private String title;

    private LocalDate releaseDate;

    private BigDecimal averageRate;
}
