package com.chubov.urlshortener.util;

import jakarta.persistence.EntityNotFoundException;

public class UrlNotFound extends EntityNotFoundException {
    //  Custom exception for catching when url not found
    public UrlNotFound(String message) {
        super(message);
    }
}
