package by.grigoryev.linkshortener.controller;

import by.grigoryev.linkshortener.dto.link.LinkStatistic;
import by.grigoryev.linkshortener.dto.link.OriginalLink;
import by.grigoryev.linkshortener.dto.link.ShortLink;
import by.grigoryev.linkshortener.service.LinkShortenerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = "Bearer Authentication")
@Tag(name = "LinkShortener", description = "The LinkShortener API")
public class LinkShortenerController {

    private final LinkShortenerService linkShortenerService;

    @Operation(
            summary = "Generate", tags = "LinkShortener",
            description = "Enter your original link and get a short one",
            requestBody = @io.swagger.v3.oas.annotations.parameters
                    .RequestBody(description = "RequestBody for original link",
                    content = @Content(schema = @Schema(implementation = OriginalLink.class),
                            examples = @ExampleObject("""
                                    {
                                      "original": "https://www.noob-club.ru/index.php?frontpage;p=45"
                                    }
                                    """))
            )
    )
    @PostMapping("/generate")
    public ResponseEntity<ShortLink> generate(@RequestBody OriginalLink originalLink) {
        return ResponseEntity.status(HttpStatus.CREATED).body(linkShortenerService.generate(originalLink));
    }

    @Operation(
            summary = "Redirect", tags = "LinkShortener",
            description = "Enter your short link and redirect to original one",
            parameters = @Parameter(name = "shortLink", description = "Enter your short link here", example = "/1/noob-club")
    )
    @GetMapping
    public ResponseEntity<Void> redirect(@RequestParam String shortLink) {
        return ResponseEntity.status(HttpStatus.PERMANENT_REDIRECT)
                .location(URI.create(linkShortenerService.redirect(shortLink).getOriginal()))
                .build();
    }

    @Operation(
            summary = "Stat", tags = "LinkShortener",
            description = "Enter your short link and get it`s stat",
            parameters = @Parameter(name = "shortLink", description = "Enter your short link here", example = "/1/noob-club")
    )
    @GetMapping("/stat")
    public ResponseEntity<LinkStatistic> stat(@RequestParam String shortLink) {
        return ResponseEntity.ok(linkShortenerService.stat(shortLink));
    }

    @Operation(
            summary = "Stats", tags = "LinkShortener",
            description = "Enter the number of pages and links statistics displayed per page",
            parameters = {
                    @Parameter(name = "page", description = "Enter page number here",
                            example = "1"),
                    @Parameter(name = "count", description = "Enter link statistics count here",
                            example = "5")
            }
    )
    @GetMapping("/stats")
    public ResponseEntity<List<LinkStatistic>> stats(@RequestParam Integer page, Integer count) {
        return ResponseEntity.ok(linkShortenerService.stats(page, count));
    }

}
