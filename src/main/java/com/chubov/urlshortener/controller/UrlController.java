package com.chubov.urlshortener.controller;

import com.chubov.urlshortener.dto.UrlDto;
import com.chubov.urlshortener.service.UrlService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.net.MalformedURLException;
import java.net.URI;

@RestController
@RequestMapping("/api")
public class UrlController {
    private final UrlService urlService;

    @Autowired
    public UrlController(UrlService urlService) {
        this.urlService = urlService;
    }

    @GetMapping()
    public String hello() {
        return "hello";
    }

    @PostMapping("/create-short")
    public String convertToShortUrl(@RequestBody @Valid UrlDto request,
                                    BindingResult bindingResult) throws MalformedURLException {
        if (bindingResult.hasErrors()) {
            throw new RuntimeException();
        }
        return urlService.convertToShortUrl(request);
    }

    @GetMapping(value = "/{shortUrl}")
    public ResponseEntity<Void> redirectToLongUrl(@PathVariable("shortUrl") String shortUrl) {
        String longUrl = urlService.getOriginalUrl(shortUrl);
        return ResponseEntity.status(HttpStatus.FOUND).location(URI.create(longUrl)).build();
    }
}
