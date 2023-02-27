package com.chubov.urlshortener.service;

import com.chubov.urlshortener.dto.UrlDto;
import com.chubov.urlshortener.entity.Url;
import com.chubov.urlshortener.repository.UrlRepository;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.auditing.CurrentDateTimeProvider;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.Optional;

@Service
public class UrlService {
    private final UrlRepository urlRepository;
    private final BaseConversationService baseConversationService;
    private final ModelMapper modelMapper;

    @Autowired
    public UrlService(UrlRepository urlRepository, BaseConversationService baseConversationService, ModelMapper modelMapper) {
        this.urlRepository = urlRepository;
        this.baseConversationService = baseConversationService;
        this.modelMapper = modelMapper;
    }

    public String convertToShortUrl(UrlDto request) {
        Url url = convertToUrl(request);
        Optional<Url> existUlr = urlRepository.findByLongUrl(url.getLongUrl());
        if(existUlr.isPresent()){
            return baseConversationService.encode(existUlr.get().getId());
        }

        url.setCreatedAt(new Date());
        //  Expires after 10 days after adding in db
        Calendar c = Calendar.getInstance();
        c.setTime(url.getCreatedAt());
        c.add(Calendar.DATE, 10);
        url.setExpiresDate(c.getTime());

        Url updatedUrl = urlRepository.save(url);

        String shortUrl = baseConversationService.encode(updatedUrl.getId());

        return shortUrl;
    }

    public String getOriginalUrl(String shortUrl) {
        Long id = baseConversationService.decode(shortUrl);
        Url url = urlRepository.findById(id)
                .orElseThrow(()-> new EntityNotFoundException("Entity with id: "+id+" is not found!"));

        if (url.getExpiresDate().before(new Date()) && url.getExpiresDate() != null){
            urlRepository.delete(url);
            throw new EntityNotFoundException("Link expired!");
        }

        return url.getLongUrl();
    }


    //  ModelMapper methods. Converters.
    private Url convertToUrl(UrlDto urlDto) {
        return modelMapper.map(urlDto, Url.class);
    }

    private UrlDto convertToUrlDto(Url url) {
        return modelMapper.map(url, UrlDto.class);
    }
}
