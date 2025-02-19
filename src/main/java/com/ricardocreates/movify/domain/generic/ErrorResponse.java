package com.ricardocreates.movify.domain.generic;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ErrorResponse
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponse {
    private String description;
    private String message;
}

