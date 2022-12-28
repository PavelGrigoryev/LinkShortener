package by.grigoryev.linkshortener.service.impl;

import by.grigoryev.linkshortener.dto.LinkStatistic;
import by.grigoryev.linkshortener.model.OriginalLink;
import by.grigoryev.linkshortener.model.ShortLink;
import by.grigoryev.linkshortener.service.LinkCrudService;
import by.grigoryev.linkshortener.service.LinkShortenerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class LinkShortenerServiceImpl implements LinkShortenerService {

    private final LinkCrudService linkCrudService;

    @Override
    public ShortLink generate(OriginalLink originalLink) {
        OriginalLink savedLink = linkCrudService.save(originalLink);

        String[] splitTwoSlash = savedLink.getOriginal().split("//");
        int indexOfUrl = splitTwoSlash[1].indexOf("/");
        String siteName = splitTwoSlash[1].substring(0, indexOfUrl);

        String[] splitSiteName = siteName.split("\\.");
        StringBuilder result = new StringBuilder("/" + savedLink.getId() + "/");

        if (splitSiteName.length < 3) {
            result.append(splitSiteName[0]);
        } else {
            result.append(splitSiteName[1]);
        }

        ShortLink shortLink = createShortLink(String.valueOf(result), savedLink.getId());
        linkCrudService.save(shortLink);

        log.info("generate {}", shortLink);
        return shortLink;
    }

    @Override
    public OriginalLink redirect(String link) {
        ShortLink shortLink = linkCrudService.findFirstByLinkOrderByIdDesc(link);
        linkCrudService.updateCount(shortLink);
        OriginalLink originalLink = linkCrudService.findById(shortLink);
        log.info("redirect {}", originalLink);
        return originalLink;
    }

    @Override
    public LinkStatistic stats(String link) {
        ShortLink shortLink = linkCrudService.findFirstByLinkOrderByIdDesc(link);
        OriginalLink originalLink = linkCrudService.findById(shortLink);

        List<ShortLink> shortLinks = linkCrudService.findAllSortedByCountDesc();

        long rank = 0;
        for (int i = 0; i < shortLinks.size(); i++) {
            if (shortLink.equals(shortLinks.get(i))) {
                rank = i + 1L;
            }
        }

        LinkStatistic linkStatistic = createLinkStatistic(shortLink, originalLink, rank);

        log.info("stats {}", linkStatistic);
        return linkStatistic;
    }

    private static ShortLink createShortLink(String result, Long id) {
        return ShortLink.builder()
                .link(result)
                .originalId(id)
                .count(0L)
                .build();
    }

    private static LinkStatistic createLinkStatistic(ShortLink shortLink, OriginalLink originalLink, long rank) {
        return LinkStatistic.builder()
                .link(shortLink.getLink())
                .original(originalLink.getOriginal())
                .rank(rank)
                .count(shortLink.getCount())
                .build();
    }

}
