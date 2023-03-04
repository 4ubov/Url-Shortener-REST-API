package com.chubov.urlshortener.util;

public class BadUrlException extends RuntimeException {
    //  Custom exception for catching when bad url format
    public BadUrlException(String message) {
        super(message);
    }
}
