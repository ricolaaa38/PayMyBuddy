package com.openclassrooms.paymybuddy.exception;

/**
 * Custom exception class for handling errors in services.
 */
public class ServiceException  extends RuntimeException {

    public ServiceException(String message) {
        super(message);
    }

    public ServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
