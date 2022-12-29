package by.grigoryev.linkshortener.service.impl;

import by.grigoryev.linkshortener.dto.LinkStatistic;
import by.grigoryev.linkshortener.dto.OriginalLink;
import by.grigoryev.linkshortener.dto.ShortLink;
import by.grigoryev.linkshortener.model.Link;
import by.grigoryev.linkshortener.service.LinkCrudService;
import by.grigoryev.linkshortener.service.LinkShortenerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Service
@RequiredArgsConstructor
public class LinkShortenerServiceImpl implements LinkShortenerService {

    private final LinkCrudService linkCrudService;

    @Override
    public ShortLink generate(OriginalLink originalLink) {
        Link link = linkCrudService.save(originalLink.getOriginal());

        String[] splitTwoSlash = link.getOriginalLink().split("//");
        int indexOfUrl = splitTwoSlash[1].indexOf("/");
        String siteName = splitTwoSlash[1].substring(0, indexOfUrl);

        String[] splitSiteName = siteName.split("\\.");
        StringBuilder result = new StringBuilder("/" + link.getId() + "/");

        if (splitSiteName.length < 3) {
            result.append(splitSiteName[0]);
        } else {
            result.append(splitSiteName[1]);
        }

        ShortLink shortLink = createShortLink(result);

        linkCrudService.updateShortLink(link, shortLink.getLink());

        log.info("generate {}", shortLink);
        return shortLink;
    }

    @Override
    public OriginalLink redirect(String shortLink) {
        Link link = linkCrudService.findFirstByShortLinkOrderByIdDesc(shortLink);
        linkCrudService.updateCount(link);

        OriginalLink originalLink = new OriginalLink();
        originalLink.setOriginal(link.getOriginalLink());

        createRank();

        log.info("redirect {}", originalLink);
        return originalLink;
    }

    @Override
    public LinkStatistic stat(String shortLink) {
        Link link = linkCrudService.findFirstByShortLinkOrderByIdDesc(shortLink);
        LinkStatistic linkStatistic = createLinkStatistic(link);
        log.info("stat {}", linkStatistic);
        return linkStatistic;
    }

    @Override
    public List<LinkStatistic> stats(Integer page, Integer count) {
        List<LinkStatistic> linkStatistics = new ArrayList<>();
        linkCrudService.findAllSortedByCountDesc()
                .forEach(link -> {
                    LinkStatistic linkStatistic = createLinkStatistic(link);
                    linkStatistics.add(linkStatistic);
                });

        log.info("stats {}", linkStatistics);
        return linkStatistics.stream()
                .skip((page * 100L) - 100)
                .limit(count)
                .toList();
    }

    private void createRank() {
        AtomicInteger rank = new AtomicInteger(1);
        linkCrudService.findAllSortedByCountDesc()
                .forEach(sortedLink -> {
                    linkCrudService.updateRank(sortedLink, rank.get());
                    rank.getAndIncrement();
                });
    }

    private static LinkStatistic createLinkStatistic(Link link) {
        return LinkStatistic.builder()
                .link(link.getShortLink())
                .original(link.getOriginalLink())
                .rank(link.getRank())
                .count(link.getCount())
                .build();
    }

    private static ShortLink createShortLink(StringBuilder result) {
        return ShortLink.builder()
                .link(result.toString())
                .build();
    }

}
