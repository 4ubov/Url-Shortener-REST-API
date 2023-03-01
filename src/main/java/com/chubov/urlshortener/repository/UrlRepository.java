package com.chubov.urlshortener.repository;

import com.chubov.urlshortener.entity.Url;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UrlRepository extends JpaRepository<Url, Long> {
    Optional<Url> findByLongUrl(String longUrl);

    Optional<Url> findByShortUrl(String shortUrl);
}
