package com.javarush.domain.exception;

public class SessionFactoryInitializationException extends RuntimeException {
    public SessionFactoryInitializationException(String message) {
        super(message);
    }
}
