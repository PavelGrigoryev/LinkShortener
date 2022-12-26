package by.grigoryev.linkshortener.controller;

import by.grigoryev.linkshortener.model.OriginalLink;
import by.grigoryev.linkshortener.model.ShortLink;
import by.grigoryev.linkshortener.service.LinkService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/links")
public class LinkController {

    private final LinkService linkService;

    @PostMapping
    public ResponseEntity<ShortLink> generate(@RequestBody OriginalLink originalLink) {
        return ResponseEntity.status(HttpStatus.CREATED).body(linkService.generate(originalLink));
    }

    @GetMapping
    public ResponseEntity<Void> redirect(@RequestParam String shortLink) {
        return ResponseEntity.status(HttpStatus.PERMANENT_REDIRECT)
                .location(URI.create(linkService.redirect(shortLink).getOriginal()))
                .build();
    }

}
