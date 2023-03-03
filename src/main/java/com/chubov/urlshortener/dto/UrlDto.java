package com.chubov.urlshortener.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.util.Date;

public class UrlDto {
    //  Fields
    @Size(min = 3, message = "Length of URL, must be greater than 3 character")
    private String longUrl;

    @Size(min = 1, message = "Length of Short URL, must be greater than 1 character")
    private String shortUrl;
    private Date expiresDate;

    //  Getters and Setters
    public String getLongUrl() {
        return longUrl;
    }

    public void setLongUrl(String longUrl) {
        this.longUrl = longUrl;
    }

    public String getShortUrl() {
        return shortUrl;
    }

    public void setShortUrl(String shortUrl) {
        this.shortUrl = shortUrl;
    }

    public Date getExpiresDate() {
        return expiresDate;
    }

    public void setExpiresDate(Date expiresDate) {
        this.expiresDate = expiresDate;
    }
}
