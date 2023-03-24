package com.chubov.urlshortener.controller;

import com.chubov.urlshortener.dto.UrlDto;
import com.chubov.urlshortener.entity.Url;
import com.chubov.urlshortener.service.UrlDtoValidatorService;
import com.chubov.urlshortener.service.UrlService;
import com.chubov.urlshortener.util.BadUrlException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
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

    @PostMapping("/api/create-short")
    @Operation(
            description = "Convert input URL to short format.",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Successfully converted!",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = {
                                            @ExampleObject(
                                                    value = "{\"shortUrl\": \"7LK\"}"
                                            )
                                    }
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Bad Request!",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = {
                                            @ExampleObject(
                                                    value = "{\n" +
                                                            "    \"statusCode\": 400,\n" +
                                                            "    \"message\": \"We'll need a valid URL, like 'yourbrnd.co/niceurl'\",\n" +
                                                            "    \"timestamp\": 1679584728598\n" +
                                                            "}"
                                            )
                                    }
                            )
                    )
            }
    )
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public Map<String, String> convertToShortUrl(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(
                            mediaType = "application/json",
                            examples = {
                                    @ExampleObject(
                                            value = "{\n" +
                                                    "    \"longUrl\":\"yourLongURL.com\"\n" +
                                                    "}"
                                    )
                            }
                    )
            )
            @RequestBody @Valid UrlDto request,
            BindingResult bindingResult) {
        Url url = convertToUrl(request);
        if (bindingResult.hasErrors()) {
            StringBuilder errorMessage = new StringBuilder();
            List<FieldError> errors = bindingResult.getFieldErrors();
            for (FieldError error : errors) {
                errorMessage.append(error.getField())
                        .append(" - ")
                        .append(error.getDefaultMessage())
                        .append(".");
            }
            //  Exception which gets from input url @Valid
            throw new BadUrlException(errorMessage.toString());
        }

        UrlDto urlDto = convertToUrlDto(urlService.convertToShortUrl(url));
        //  Created (status code: 201)
        Map<String, String> response = new HashMap<>();
        response.put("shortUrl", urlDto.getShortUrl());
        return response;

    }

    @GetMapping(value = "/api/get-long/{shortUrl}")
    @Operation(
            description = "Decode shortUrl into longUrl representation.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Url successfully found!",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = {
                                            @ExampleObject(
                                                    value = """
                                                            {
                                                                "longUrl":"yourLongURL.com"
                                                            }"""
                                            )
                                    }
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Not Found!",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = {
                                            @ExampleObject(
                                                    value = "{\n" +
                                                            "  \"statusCode\": 404,\n" +
                                                            "  \"message\": \"This shortUrl doesn't exist or his duration was expired\",\n" +
                                                            "  \"timestamp\": 1679587416465\n" +
                                                            "}"
                                            )
                                    }
                            )
                    )
            }
    )
    public Map<String, String> getLongUrl(@PathVariable("shortUrl") String shortUrl) {
        Url url = urlService.getOriginalUrl(shortUrl);
        Map<String, String> response = new HashMap<>();
        response.put("longUrl", url.getLongUrl());
        return response;
    }

    @GetMapping(value = "/{shortUrl}")
    @Operation(
            description = "Redirect to Long Url by shortUrl",
            responses = {
                    @ApiResponse(
                            responseCode = "302",
                            description = "Url successfully found and redirected!",
                            content = @Content(
                                    mediaType = "application/http",
                                    examples = {
                                            @ExampleObject(
                                                    value = "\"redirect : http://yourLongURL.com\"" +
                                                            "\n<html>...</html>"
                                            )
                                    }
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Not Found!",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = {
                                            @ExampleObject(
                                                    value = "{\n" +
                                                            "  \"statusCode\": 404,\n" +
                                                            "  \"message\": \"This shortUrl doesn't exist or his duration was expired\",\n" +
                                                            "  \"timestamp\": 1679587416465\n" +
                                                            "}"
                                            )
                                    }
                            )
                    )
            }
    )
    public ResponseEntity<Url> redirectToLongUrl(@PathVariable("shortUrl") String shortUrl) {
        Url url = urlService.getOriginalUrl(shortUrl);
        return ResponseEntity.status(HttpStatus.FOUND).location(URI.create(url.getLongUrl())).build();
    }

    //  Model Mapper converters
    private Url convertToUrl(UrlDto urlDto) {
        //  Url Validation
        urlDtoValidatorService.validLongUrlDto(urlDto);
        return modelMapper.map(urlDto, Url.class);
    }

    private UrlDto convertToUrlDto(Url url) {
        return modelMapper.map(url, UrlDto.class);
    }
}
