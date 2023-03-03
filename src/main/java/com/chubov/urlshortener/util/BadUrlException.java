package com.chubov.urlshortener.util;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.net.MalformedURLException;

public class BadUrlException extends MalformedURLException {
    //  Custom exception for catching MalformedURLException when validating Url
    public BadUrlException(String message) {
        super(message);
    }
}
