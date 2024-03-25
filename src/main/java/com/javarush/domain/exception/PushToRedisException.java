package com.javarush.domain.exception;

public class PushToRedisException extends RuntimeException{
    public PushToRedisException(String message) {
        super(message);
    }
}
