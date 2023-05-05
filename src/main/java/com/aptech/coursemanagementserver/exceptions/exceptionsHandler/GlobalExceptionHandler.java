package com.aptech.coursemanagementserver.exceptions.exceptionsHandler;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import com.aptech.coursemanagementserver.dtos.ErrorDetails;
import com.aptech.coursemanagementserver.exceptions.ResourceNotFoundException;
import com.aptech.coursemanagementserver.exceptions.UserNotFoundException;

@ControllerAdvice // Allows handling exceptions across the whole application in one global
                  // handling component
public class GlobalExceptionHandler {
        // Handle global Exception
        // @ExceptionHandler(Exception.class)
        // public ResponseEntity<ErrorDetails> handleInternalException(Exception
        // resourceNotFoundException,
        // WebRequest webRequest) {
        // ErrorDetails errorDetails = new ErrorDetails(LocalDateTime.now(),
        // resourceNotFoundException.getMessage(),
        // webRequest.getDescription(false),
        // HttpStatus.INTERNAL_SERVER_ERROR.toString());

        // return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
        // }

        // 403 Handle Security Authenticate
        @ExceptionHandler(Throwable.class)
        public ResponseEntity<ErrorDetails> handleAllExceptions(AccessDeniedException t) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ErrorDetails.builder()
                                .statusCode("403")
                                .timestamp(LocalDateTime.now())
                                .message("Forbidden").build());
        }

        // 404
        @ExceptionHandler(UserNotFoundException.class)
        public final ResponseEntity<ErrorDetails> handleUserNotFoundException(Exception ex, WebRequest request)
                        throws Exception {
                ErrorDetails errorDetails = new ErrorDetails(LocalDateTime.now(),
                                ex.getMessage(), request.getDescription(false), HttpStatus.NOT_FOUND.toString());

                return new ResponseEntity<ErrorDetails>(errorDetails, HttpStatus.NOT_FOUND);
        }

        @ExceptionHandler(ResourceNotFoundException.class)
        public ResponseEntity<ErrorDetails> handleResourceNotFoundException(
                        ResourceNotFoundException resourceNotFoundException, WebRequest webRequest) {
                ErrorDetails errorDetails = new ErrorDetails(LocalDateTime.now(),
                                resourceNotFoundException.getMessage(),
                                webRequest.getDescription(false), HttpStatus.NOT_FOUND.toString());

                return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
        }

}
