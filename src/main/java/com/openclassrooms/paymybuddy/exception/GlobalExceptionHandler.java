package com.openclassrooms.paymybuddy.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Global exception handler for managing application errors.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LogManager.getLogger(GlobalExceptionHandler.class);

    /**
     * Handles ServiceException and returns a 500 Internal Server Error response.
     *
     * @param ex the ServiceException to handle
     * @return ResponseEntity with error message and status
     */
    @ExceptionHandler(ServiceException.class)
    public ResponseEntity<?> handleServiceException(ServiceException ex) {
        logger.error("Service Exception: {}", ex.getMessage(), ex);
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Handles ControllerException and returns a 400 Bad Request response.
     *
     * @param ex the ControllerException to handle
     * @return ResponseEntity with error message and status
     */
    @ExceptionHandler(ControllerException.class)
    public ResponseEntity<?> handleControllerException(ControllerException ex) {
        logger.error("Controller Exception: {}", ex.getMessage(), ex);
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles any other unexpected exceptions and returns a 500 Internal Server Error response.
     *
     * @param ex the Exception to handle
     * @return ResponseEntity with generic error message and status
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGlobalException(Exception ex) {
        logger.error("Unexpected error: {}", ex.getMessage(), ex);
        return new ResponseEntity<>("An unexpected error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
