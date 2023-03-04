package com.chubov.urlshortener.service;

import com.chubov.urlshortener.dto.UrlDto;
import com.chubov.urlshortener.util.BadUrlException;
import org.apache.commons.validator.routines.UrlValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.MalformedURLException;
import java.net.URL;

@Service
public class UrlDtoValidatorService {
    //  Service for Validating url format.

    //  Fields
    private final UrlValidator urlValidator;

    @Autowired
    public UrlDtoValidatorService(UrlValidator urlValidator) {
        this.urlValidator = urlValidator;
    }

    //  Validating that Url is correct.
    public void validLongUrlDto(UrlDto urlDto) {
        urlDto.setLongUrl(urlDto.getLongUrl().trim());
        URL tmp = null;
        try {
            tmp = new URL(urlDto.getLongUrl());
            urlDto.setLongUrl("http://" + tmp.getHost().toLowerCase() + tmp.getFile());
        } catch (MalformedURLException e) {
            try {
                tmp = new URL("http://" + (urlDto.getLongUrl()));
                urlDto.setLongUrl(tmp.getProtocol() + "://" + tmp.getHost().toLowerCase() + tmp.getFile());
            } catch (MalformedURLException exception) {
                throw new BadUrlException("We'll need a valid URL, like 'yourbrnd.co/niceurl'");
            }
        }

        if (!urlValidator.isValid(urlDto.getLongUrl())) {
            throw new BadUrlException("We'll need a valid URL, like 'yourbrnd.co/niceurl'");
        }
    }
}
