package com.ricardocreates.movify.infra.data.jpa.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@ToString
@Entity
@Table(schema = "MOVIFY", name = "MOVIES")
public class MovieEntity {
    @Id
    @Column(name = "ID")
    private Long id;

    @Column(name = "TITLE")
    private String title;

    @Column(name = "RELEASE_DATE")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate releaseDate;

    @Column(name = "POSTER_URL")
    private String posterUrl;

    @Column(name = "OVERVIEW")
    private String overview;

    @Column(name = "RUNTIME")
    private Integer runtime;

    @Column(name = "RATING")
    private BigDecimal rating;

    @Column(name = "LANGUAGE")
    private String language;

    @ManyToMany(cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
    }, fetch = FetchType.LAZY)
    @JoinTable(
            schema = "MOVIFY",
            name = "MOVIES_GENRES",
            joinColumns = {@JoinColumn(name = "movie_id")},
            inverseJoinColumns = {@JoinColumn(name = "genre_id")}
    )
    private Set<GenreEntity> genres = new HashSet<>();
}