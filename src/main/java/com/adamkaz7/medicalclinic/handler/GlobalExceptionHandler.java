package com.adamkaz7.medicalclinic.handler;

import com.adamkaz7.medicalclinic.exception.MedicalClinicException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MedicalClinicException.class)
    public ProblemDetail handleMedicalClinicException(MedicalClinicException exception) {
        return ProblemDetail.forStatusAndDetail(exception.getStatus(), exception.getMessage());
    }

    @ExceptionHandler(RuntimeException.class)
    public ProblemDetail handleRuntimeException(RuntimeException exception) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, "Unknown error");
    }
}
