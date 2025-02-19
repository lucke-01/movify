package com.ricardocreates.movify.infra.data.jpa.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Entity
@Table(schema = "MOVIFY", name = "API_KEY")
public class ApiKeyEntity {
    @Id
    private Long id;

    @Column(unique = true)
    private String keyValue;
}
