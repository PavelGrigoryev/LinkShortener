package by.grigoryev.linkshortener.controller;

import by.grigoryev.linkshortener.dto.LinkStatistic;
import by.grigoryev.linkshortener.model.OriginalLink;
import by.grigoryev.linkshortener.model.ShortLink;
import by.grigoryev.linkshortener.service.LinkShortenerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequiredArgsConstructor
public class LinkController {

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

    @GetMapping("/stats")
    public ResponseEntity<LinkStatistic> stats(@RequestParam String shortLink) {
        return ResponseEntity.ok(linkShortenerService.stats(shortLink));
    }

}
