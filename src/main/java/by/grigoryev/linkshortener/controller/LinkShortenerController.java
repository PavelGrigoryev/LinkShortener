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
import java.util.List;

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
    public ResponseEntity<LinkStatistic> stats(@RequestParam String shortLink) {
        return ResponseEntity.ok(linkShortenerService.stats(shortLink));
    }

    @GetMapping("/stats")
    public ResponseEntity<List<LinkStatistic>> stats(@RequestParam int page, int count) {
        return ResponseEntity.ok(linkShortenerService.stats(page, count));
    }

}
