package com.chubov.urlshortener.controller;

import com.chubov.urlshortener.dto.UrlDto;
import com.chubov.urlshortener.entity.Url;
import com.chubov.urlshortener.service.UrlDtoValidatorService;
import com.chubov.urlshortener.service.UrlService;
import com.chubov.urlshortener.util.ErrorResponse;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.net.MalformedURLException;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api")
public class UrlController {
    private final UrlService urlService;
    private final UrlDtoValidatorService urlDtoValidatorService;
    private final ModelMapper modelMapper;

    @Autowired
    public UrlController(UrlService urlService,
                         UrlDtoValidatorService urlDtoValidatorService,
                         ModelMapper modelMapper) {
        this.urlService = urlService;
        this.urlDtoValidatorService = urlDtoValidatorService;
        this.modelMapper = modelMapper;
    }

    @PostMapping("/create-short")
    public ResponseEntity<?> convertToShortUrl(@RequestBody @Valid UrlDto request,
                                               BindingResult bindingResult) {
        try {
            Url url = convertToUrl(request);
            if (bindingResult.hasErrors()) {
                StringBuilder errorMessage = new StringBuilder();
                List<FieldError> errors = bindingResult.getFieldErrors();
                for (FieldError error : errors) {
                    errorMessage.append(error.getField())
                            .append(" - ")
                            .append(error.getDefaultMessage())
                            .append("; ");
                }
                //  Exception which gets from input url @Valid
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(new ErrorResponse(errorMessage.toString(), System.currentTimeMillis()));
            }

            UrlDto urlDto = convertToUrlDto(urlService.convertToShortUrl(url));
            //  Created (status code: 201)
            return ResponseEntity.status(HttpStatus.CREATED)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(urlDto);

        } catch (MalformedURLException exception) {
            //  Exception which gets from UrlDtoValidator (Bad Url Format)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(new ErrorResponse(exception.getMessage(), System.currentTimeMillis()));
        }
    }

    @GetMapping(value = "/{shortUrl}")
    public ResponseEntity<Void> redirectToLongUrl(@PathVariable("shortUrl") String shortUrl) {
        String longUrl = urlService.getOriginalUrl(shortUrl);
        return ResponseEntity.status(HttpStatus.FOUND).location(URI.create(longUrl)).build();
    }


    //  Model Mapper converters
    private Url convertToUrl(UrlDto urlDto) throws MalformedURLException {
        //  Url Validation
        urlDtoValidatorService.validLongUrlDto(urlDto);
        return modelMapper.map(urlDto, Url.class);
    }

    private UrlDto convertToUrlDto(Url url) {
        return modelMapper.map(url, UrlDto.class);
    }
}
