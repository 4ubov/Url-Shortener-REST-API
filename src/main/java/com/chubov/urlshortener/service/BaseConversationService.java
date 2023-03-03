package com.chubov.urlshortener.service;

import org.hashids.Hashids;
import org.springframework.stereotype.Service;

@Service
public class BaseConversationService {
    //  This service using for converting url from 10 base to 62 base. (encoding/decoding to short link)

    //  Fields
    private static final String allowedString = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    //  Encoding methods
    public String encode(Long id) {
        Hashids hashids = new Hashids("SomeCoolSalt" + id);
        String encoded = hashids.encode(id);

        return encoded;
    }

    public String encodeWithAnotherSalt(Long id, String longUrl) {
        Hashids hashids = new Hashids("SomeCoolSalt" + id + longUrl);
        String encoded = hashids.encode(id);

        return encoded;
    }

    //  Decoding methods
    public long[] decode(Long id, String shortUrl) {
        Hashids hashids = new Hashids("SomeCoolSalt" + id);
        return hashids.decode(shortUrl);
    }

    public long[] decodeWithAnotherSalt(Long id, String shortUrl) {
        Hashids hashids = new Hashids("SomeCoolSalt" + id + shortUrl);
        return hashids.decode(shortUrl);
    }

}
