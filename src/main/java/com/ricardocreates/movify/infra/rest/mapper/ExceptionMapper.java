package com.ricardocreates.movify.infra.rest.mapper;

import com.ricardocreates.movify.domain.exception.MovifyBaseException;
import com.ricardocreates.movify.domain.generic.ErrorResponse;
import com.swagger.client.codegen.rest.model.ErrorResponseDTO;
import com.swagger.client.codegen.rest.model.GenericErrorResponseDTO;
import org.mapstruct.Mapper;
import org.springframework.http.ResponseEntity;

import java.util.Map;

@Mapper(componentModel = "spring")
public interface ExceptionMapper {
    default ResponseEntity<GenericErrorResponseDTO> baseExceptionToResponseEntity(
            MovifyBaseException aviationBaseException) {
        GenericErrorResponseDTO errorResponseDto = new GenericErrorResponseDTO();
        errorResponseDto.setErrorCode(String.valueOf(aviationBaseException.getHttpCode()));
        errorResponseDto.setMessage(aviationBaseException.getMessage());

        errorResponseDto.setErrors(errorResponseMapFromDomain(aviationBaseException.getErrors()));

        return ResponseEntity
                .status(aviationBaseException.getHttpCode())
                .body(errorResponseDto);
    }

    ErrorResponseDTO errorResponseFromDomain(ErrorResponse errorResponse);

    Map<String, ErrorResponseDTO> errorResponseMapFromDomain(Map<String, ErrorResponse> errors);
}
