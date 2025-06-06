package com.openclassrooms.paymybuddy.security;

/**
 * Interface for password encryption.
 */
public interface PasswordEncoderService {
    String encode(String rawPassword);
    boolean matches(String rawPassword, String encodedPassword);
}
