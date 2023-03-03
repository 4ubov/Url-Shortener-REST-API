package com.chubov.urlshortener.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.util.Date;

@Entity
@Table(name = "url")
public class Url {
    //  Fields
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "url_id")
    private Long id;

    @Column(name = "long_url")
    @NotEmpty(message = "Url should not be empty.")
    @Size(min = 3, message = "Length of URL, must be greater than 3 character")
    private String longUrl;

    @Column(name = "short_url")
    @Size(min = 1, message = "Length of Short URL, must be greater than 1 character")
    private String shortUrl;

    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @Column(name = "expires_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date expiresDate;


    //  Constructors
    public Url() {
    }

    public Url(String longUrl, String shortUrl, Date createdAt, Date expiresDate) {
        this.longUrl = longUrl;
        this.shortUrl = shortUrl;
        this.createdAt = createdAt;
        this.expiresDate = expiresDate;
    }

    //  Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getExpiresDate() {
        return expiresDate;
    }

    public void setExpiresDate(Date expiresDate) {
        this.expiresDate = expiresDate;
    }

    //  Overriding methods
    @Override
    public String toString() {
        return "Url{" +
                "id=" + id +
                ", longUrl='" + longUrl + '\'' +
                ", shortUrl='" + shortUrl + '\'' +
                ", createdAt=" + createdAt +
                ", expiresDate=" + expiresDate +
                '}';
    }
}
