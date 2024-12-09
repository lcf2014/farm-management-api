package com.luana.farm.exception;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ApiError {

    private int status;
    private String message;
    private List<ValidationError> errors;

    public ApiError(int status, String message, List<ValidationError> errors) {
        this.status = status;
        this.message = message;
        this.errors = errors;
    }

    @Getter
    @Setter
    public static class ValidationError {
        private String field;
        private String message;

        public ValidationError(String field, String message) {
            this.field = field;
            this.message = message;
        }
    }
}
