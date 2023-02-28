package com.chubov.urlshortener.service;

import com.chubov.urlshortener.dto.UrlDto;
import org.apache.commons.validator.routines.UrlValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.MalformedURLException;

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
        if (!urlValidator.isValid(urlDto.getLongUrl())) {
            if(!urlValidator.isValid("http://"+(urlDto.getLongUrl()))){
                throw new MalformedURLException("We'll need a valid URL, like \"yourbrnd.co/niceurl\"");
            }
            else {
                urlDto.setLongUrl("http://"+urlDto.getLongUrl());
            }
        }
    }
}
