package com.ricardocreates.movify.infra.rest;

import com.ricardocreates.movify.domain.exception.MovifyBaseException;
import com.ricardocreates.movify.infra.rest.mapper.ExceptionMapper;
import com.swagger.client.codegen.rest.model.ErrorResponseDTO;
import com.swagger.client.codegen.rest.model.GenericErrorResponseDTO;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@ControllerAdvice
@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE)
public class RestExceptionHandler {
    private final ExceptionMapper exceptionMapper;

    @ExceptionHandler(MovifyBaseException.class)
    protected ResponseEntity<GenericErrorResponseDTO> handleEntityServiceUnavailable(
            MovifyBaseException aviationBaseException) {
        return exceptionMapper.baseExceptionToResponseEntity(aviationBaseException);
    }

    /**
     * this will be triggered when some method parameter is missing
     *
     * @param reqException reqException
     * @return response entity
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    protected ResponseEntity<GenericErrorResponseDTO> handleEntityServiceUnavailable(
            MissingServletRequestParameterException reqException) {
        final GenericErrorResponseDTO errorResponseDto = new GenericErrorResponseDTO();
        errorResponseDto.setErrorCode(String.valueOf(HttpStatus.BAD_REQUEST.value()));
        errorResponseDto.setMessage(reqException.getMessage());

        Map<String, ErrorResponseDTO> errors = new HashMap<>();
        ErrorResponseDTO error = new ErrorResponseDTO();
        error.setMessage("Missing parameter");
        error.setDescription(String.format("parameter type: %s", reqException.getParameterType()));
        errors.put(reqException.getParameterName(), error);
        errorResponseDto.setErrors(errors);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponseDto);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    protected ResponseEntity<GenericErrorResponseDTO> handleEntityServiceUnavailable(
            MethodArgumentTypeMismatchException missMatch) {
        final GenericErrorResponseDTO errorResponseDto = new GenericErrorResponseDTO();
        errorResponseDto.setErrorCode(String.valueOf(HttpStatus.BAD_REQUEST.value()));
        errorResponseDto.setMessage(missMatch.getMessage());

        Map<String, ErrorResponseDTO> errors = new HashMap<>();
        ErrorResponseDTO error = new ErrorResponseDTO();
        error.setMessage("Missing parameter");
        error.setDescription(
                String.format("parameter type: %s", missMatch.getParameter().getParameterType()));
        errors.put(missMatch.getParameter().getParameterName(), error);
        errorResponseDto.setErrors(errors);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponseDto);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<GenericErrorResponseDTO> handleConstraintViolationException(
            ConstraintViolationException cvException) {
        GenericErrorResponseDTO errorResponseDto = new GenericErrorResponseDTO();
        errorResponseDto.setErrorCode(String.valueOf(HttpStatus.BAD_REQUEST.value()));
        errorResponseDto.setMessage("Bad request parameter");
        Map<String, ErrorResponseDTO> errors = new HashMap<>();
        for (ConstraintViolation<?> violation : cvException.getConstraintViolations()) {
            String propertyPath = violation.getPropertyPath().toString();
            String message = violation.getMessage();
            ErrorResponseDTO errorResponse = new ErrorResponseDTO();
            errorResponse.setDescription(message);
            errorResponse.setMessage(message);
            errors.put(propertyPath, errorResponse);
        }
        errorResponseDto.setErrors(errors);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponseDto);
    }
}
