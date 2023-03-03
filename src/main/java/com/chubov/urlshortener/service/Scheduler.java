package com.chubov.urlshortener.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class Scheduler {
    //  Service for setting Schedule some actions in db.

    //  Fields
    private final UrlService urlService;

    @Autowired
    public Scheduler(UrlService urlService) {
        this.urlService = urlService;
    }

    //  Deleting Expired urls every day at midnight (By Cron)
    @Scheduled(cron = "0 0 0 * * ?", zone = "GMT+6")
    public void deleteExpiredUrl() {
        urlService.deleteExpiredUrl();
    }
}
