package com.aptech.coursemanagementserver.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
public class InvalidTokenException extends RuntimeException {

    private HttpStatus status;
    private String message;

    public InvalidTokenException(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }

    public InvalidTokenException(String message, HttpStatus status, String message1) {
        super(message);
        this.status = status;
        this.message = message1;
    }

    public HttpStatus getStatus() {
        return status;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
