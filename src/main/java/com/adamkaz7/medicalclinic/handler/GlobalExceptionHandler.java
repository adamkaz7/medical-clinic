package com.adamkaz7.medicalclinic.handler;

import com.adamkaz7.medicalclinic.exception.MedicalClinicException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(MedicalClinicException.class)
    public ProblemDetail handleMedicalClinicException(MedicalClinicException exception) {
        log.warn("Request rejected: exception={}, status={}",
                exception.getClass().getSimpleName(), exception.getStatus().value());
        return ProblemDetail.forStatusAndDetail(exception.getStatus(), exception.getMessage());
    }

    @ExceptionHandler(RuntimeException.class)
    public ProblemDetail handleRuntimeException(RuntimeException exception) {
        log.error("Unexpected error while handling request: status=500", exception);
        return ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, "Unknown error");
    }
}
