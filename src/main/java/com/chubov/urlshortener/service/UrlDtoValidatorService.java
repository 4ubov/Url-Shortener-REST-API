package com.chubov.urlshortener.service;

import com.chubov.urlshortener.dto.UrlDto;
import org.apache.commons.validator.routines.UrlValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.MalformedURLException;
import java.net.URL;

@Service
public class UrlDtoValidatorService {
    private final UrlValidator urlValidator;

    @Autowired
    public UrlDtoValidatorService(UrlValidator urlValidator) {
        this.urlValidator = urlValidator;
    }

    //  Validate that Url is correct.
    public void validLongUrlDto(UrlDto urlDto) throws MalformedURLException {
        urlDto.setLongUrl(urlDto.getLongUrl().trim());
        URL tmp = null;
        try {
            tmp = new URL(urlDto.getLongUrl());
            urlDto.setLongUrl("http://" + tmp.getHost().toLowerCase() + tmp.getFile());
        } catch (MalformedURLException e) {
            tmp = new URL("http://" + (urlDto.getLongUrl()));
            urlDto.setLongUrl(tmp.getProtocol() + "://" + tmp.getHost().toLowerCase() + tmp.getFile());
        }

        if (!urlValidator.isValid(urlDto.getLongUrl())) {
            throw new MalformedURLException("We'll need a valid URL, like \"yourbrnd.co/niceurl\"");
        }
    }
}
