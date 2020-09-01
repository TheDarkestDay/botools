package com.abrenchev.exceptions;

public class BotoolsException extends RuntimeException {
    public BotoolsException(String message, Exception cause) {
        super(message, cause);
    }
}
