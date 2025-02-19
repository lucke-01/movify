package com.ricardocreates.movify.domain.generic;

import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public class GenericErrorResponse {
    private String code;
    private String message;

    @Builder.Default
    private List<ErrorResponse> errors = new ArrayList<>();
}
