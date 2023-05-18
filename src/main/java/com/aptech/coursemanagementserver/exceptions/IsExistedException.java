package com.aptech.coursemanagementserver.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class IsExistedException extends RuntimeException {

    public IsExistedException(String email) {
        super(String.format("The %s have already existed.", email));
    }

    public IsExistedException(String message, Throwable cause) {
        super(message, cause);
    }
}
