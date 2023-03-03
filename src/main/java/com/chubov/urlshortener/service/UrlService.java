package com.chubov.urlshortener.service;

import com.chubov.urlshortener.entity.Url;
import com.chubov.urlshortener.repository.UrlRepository;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UrlService {
    //  Main service for manipulating with Url.

    //  Fields
    private final UrlRepository urlRepository;
    private final BaseConversationService baseConversationService;
    private final ModelMapper modelMapper;
    private final UrlDtoValidatorService urlDtoValidatorService;

    @Autowired
    public UrlService(UrlRepository urlRepository,
                      BaseConversationService baseConversationService,
                      ModelMapper modelMapper, UrlDtoValidatorService urlDtoValidatorService) {
        this.urlRepository = urlRepository;
        this.baseConversationService = baseConversationService;
        this.modelMapper = modelMapper;
        this.urlDtoValidatorService = urlDtoValidatorService;
    }

    // Return new Url Entity with converted longUrl to shortUrl
    public Url convertToShortUrl(Url url) {
        Optional<Url> existUlr = urlRepository.findByLongUrl(url.getLongUrl());
        if (existUlr.isPresent()) {
            return existUlr.get();
        }
        url.setCreatedAt(new Date());
        Calendar c = Calendar.getInstance();
        c.setTime(url.getCreatedAt());
        c.add(Calendar.DATE, 10);
        url.setExpiresDate(c.getTime());
        Optional<Url> existUlr3 = urlRepository.findByLongUrl(url.getLongUrl());
        if (existUlr3.isPresent()) {
            return existUlr3.get();
        }
        urlRepository.save(url);
        String shortUrl = baseConversationService.encode(url.getId());
        Optional<Url> tmpUrl = urlRepository.findByShortUrl(shortUrl);
        while (tmpUrl.isPresent()) {
            if (url.getLongUrl().equals(tmpUrl.get().getLongUrl())
                    &&
                    !Objects.equals(url.getId(), tmpUrl.get().getId())) {
                urlRepository.delete(url);
                return tmpUrl.get();
            }

            shortUrl = baseConversationService.encodeWithAnotherSalt(url.getId(), url.getLongUrl());
            tmpUrl = (urlRepository.findByShortUrl(shortUrl));
        }
        url.setShortUrl(shortUrl);
        Url result = urlRepository.save(url);
        return result;
    }

    //  Return longUrl from db is it exist
    public String getOriginalUrl(String shortUrl) {
        Url url = urlRepository.findByShortUrl(shortUrl)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND,
                                "Etity with this shortUrl: " + shortUrl + " , is not found!",
                                new EntityNotFoundException("Etity with this shortUrl: " + shortUrl + " , is not found!")));
        if (url.getExpiresDate().before(new Date()) && url.getExpiresDate() != null) {
            urlRepository.delete(url);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Link is expired!",
                    new EntityNotFoundException("Link is expired!"));
        }

        return url.getLongUrl();
    }

    //  Delete all expired urls
    public void deleteExpiredUrl() {
        Date now = new Date();
        Optional<List<Url>> urls = Optional.of(urlRepository.findAll()
                .stream()
                .filter(url -> url.getExpiresDate().getTime() < now.getTime())
                .collect(Collectors.toList()));
        urls.ifPresent(urlRepository::deleteAll);
    }
}
