package by.grigoryev.linkshortener.controller;

import by.grigoryev.linkshortener.dto.LinkStatistic;
import by.grigoryev.linkshortener.dto.OriginalLink;
import by.grigoryev.linkshortener.dto.ShortLink;
import by.grigoryev.linkshortener.service.LinkShortenerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class LinkShortenerController {

    private final LinkShortenerService linkShortenerService;

    @PostMapping("/generate")
    public ResponseEntity<ShortLink> generate(@RequestBody OriginalLink originalLink) {
        return ResponseEntity.status(HttpStatus.CREATED).body(linkShortenerService.generate(originalLink));
    }

    @GetMapping
    public ResponseEntity<Void> redirect(@RequestParam String shortLink) {
        return ResponseEntity.status(HttpStatus.PERMANENT_REDIRECT)
                .location(URI.create(linkShortenerService.redirect(shortLink).getOriginal()))
                .build();
    }

    @GetMapping("/stat")
    public ResponseEntity<LinkStatistic> stat(@RequestParam String shortLink) {
        return ResponseEntity.ok(linkShortenerService.stat(shortLink));
    }

    @GetMapping("/stats")
    public ResponseEntity<List<LinkStatistic>> stats(@RequestParam Integer page, Integer count) {
        return ResponseEntity.ok(linkShortenerService.stats(page, count));
    }

}
