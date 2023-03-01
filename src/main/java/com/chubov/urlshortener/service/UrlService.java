package com.chubov.urlshortener.service;

import com.chubov.urlshortener.dto.UrlDto;
import com.chubov.urlshortener.entity.Url;
import com.chubov.urlshortener.repository.UrlRepository;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.MalformedURLException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UrlService {
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

    public String convertToShortUrl(UrlDto request) throws MalformedURLException {
        Url url = convertToUrl(request);
        Optional<Url> existUlr = urlRepository.findByLongUrl(url.getLongUrl());
        if (existUlr.isPresent()) {
            return existUlr.get().getShortUrl();
        }
        url.setCreatedAt(new Date());
        Calendar c = Calendar.getInstance();
        c.setTime(url.getCreatedAt());
        c.add(Calendar.DATE, 10);
        url.setExpiresDate(c.getTime());
        Optional<Url> existUlr3 = urlRepository.findByLongUrl(url.getLongUrl());
        if (existUlr3.isPresent()) {
            return existUlr3.get().getShortUrl();
        }
        urlRepository.save(url);
        String shortUrl = baseConversationService.encode(url.getId());
        Optional<Url> tmpUrl = urlRepository.findByShortUrl(shortUrl);
        while (tmpUrl.isPresent()) {
            if (url.getLongUrl().equals(tmpUrl.get().getLongUrl())
                    &&
                    !Objects.equals(url.getId(), tmpUrl.get().getId())) {
                System.out.println("Лонг урл есть в бд скинул шорт:");
                urlRepository.delete(url);
                return tmpUrl.get().getShortUrl();
            }

            shortUrl = baseConversationService.encodeWithAnotherSalt(url.getId(), url.getLongUrl());
            System.out.println("Generated new: " + shortUrl);
            tmpUrl = (urlRepository.findByShortUrl(shortUrl));
            System.out.println(tmpUrl);
        }
        url.setShortUrl(shortUrl);
        urlRepository.save(url);
        return shortUrl;
    }

    public String getOriginalUrl(String shortUrl) {
        Url url = urlRepository.findByShortUrl(shortUrl)
                .orElseThrow(() ->
                        new EntityNotFoundException("Etity with this shortUrl: " + shortUrl + " , is not found!"));
        if (url.getExpiresDate().before(new Date()) && url.getExpiresDate() != null) {
            urlRepository.delete(url);
            throw new EntityNotFoundException("Link expired!");
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

    //  ModelMapper methods. Converters.
    private Url convertToUrl(UrlDto urlDto) throws MalformedURLException {
        //  Url Validation
        urlDtoValidatorService.validLongUrlDto(urlDto);

        return modelMapper.map(urlDto, Url.class);
    }

    private UrlDto convertToUrlDto(Url url) {
        return modelMapper.map(url, UrlDto.class);
    }
}
