package com.chubov.urlshortener.dto;

import java.util.Date;

public class UrlDto {
    //  Fields
    private String longUrl;

    private Date expiresDate;

    //  Getters and Setters
    public String getLongUrl() {
        return longUrl;
    }

    public void setLongUrl(String longUrl) {
        this.longUrl = longUrl;
    }

    public Date getExpiresDate() {
        return expiresDate;
    }

    public void setExpiresDate(Date expiresDate) {
        this.expiresDate = expiresDate;
    }


}
