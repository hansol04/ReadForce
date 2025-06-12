package com.readforce.controller;

import com.readforce.service.NewsScraperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/news")
public class NewsScraperController {

    @Autowired
    private NewsScraperService newsScraperService;

    @GetMapping("/fulltext")
    public String getFullArticle(@RequestParam("url") String url) {
        return newsScraperService.scrapeArticleContent(url);
    }

}
