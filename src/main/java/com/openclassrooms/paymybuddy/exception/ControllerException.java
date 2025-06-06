package com.openclassrooms.paymybuddy.exception;

/**
 * Custom exception class for handling errors in controllers.
 */
public class ControllerException extends RuntimeException {

    public ControllerException(String message) {
        super(message);
    }

    public ControllerException(String message, Throwable cause) {
        super(message, cause);
    }
}
