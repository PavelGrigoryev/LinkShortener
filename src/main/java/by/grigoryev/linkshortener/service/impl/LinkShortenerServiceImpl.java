package by.grigoryev.linkshortener.service.impl;

import by.grigoryev.linkshortener.dto.LinkStatistic;
import by.grigoryev.linkshortener.dto.OriginalLink;
import by.grigoryev.linkshortener.dto.ShortLink;
import by.grigoryev.linkshortener.exception.PageFormatException;
import by.grigoryev.linkshortener.mapper.LinkMapper;
import by.grigoryev.linkshortener.model.Link;
import by.grigoryev.linkshortener.service.LinkCrudService;
import by.grigoryev.linkshortener.service.LinkShortenerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Service
@RequiredArgsConstructor
public class LinkShortenerServiceImpl implements LinkShortenerService {

    private static final int NUMBER_OF_URL_PARTS = 3;
    private static final int RANK_START_NUMBER = 1;
    private static final long PAGE_SIZE = 100L;

    private final LinkCrudService linkCrudService;
    private final LinkMapper linkMapper = Mappers.getMapper(LinkMapper.class);

    @Override
    public ShortLink generate(OriginalLink originalLink) {
        Link link = linkCrudService.save(originalLink.getOriginal());

        String[] splitTwoSlash = link.getOriginalLink().split("//");
        int indexOfUrl = splitTwoSlash[1].indexOf("/");
        String siteName = splitTwoSlash[1].substring(0, indexOfUrl);

        String[] splitSiteName = siteName.split("\\.");
        StringBuilder result = new StringBuilder("/" + link.getId() + "/");

        if (splitSiteName.length < NUMBER_OF_URL_PARTS) {
            result.append(splitSiteName[0]);
        } else {
            result.append(splitSiteName[1]);
        }

        Link updatedLink = linkCrudService.updateShortLink(link, result.toString());
        ShortLink shortLink = linkMapper.toShortLink(updatedLink);

        log.info("generate {}", shortLink);
        return shortLink;
    }

    @Override
    public OriginalLink redirect(String shortLink) {
        Link link = linkCrudService.findFirstByShortLinkOrderByIdDesc(shortLink);
        linkCrudService.updateCount(link);

        OriginalLink originalLink = linkMapper.toOriginalLink(link);

        createRank();

        log.info("redirect {}", originalLink);
        return originalLink;
    }

    @Override
    public LinkStatistic stat(String shortLink) {
        Link link = linkCrudService.findFirstByShortLinkOrderByIdDesc(shortLink);
        LinkStatistic linkStatistic = linkMapper.toLinkStatistic(link);
        log.info("stat {}", linkStatistic);
        return linkStatistic;
    }

    @Override
    public List<LinkStatistic> stats(Integer page, Integer count) {
        checkPageFormat(page, count);

        List<Link> links = linkCrudService.findAllSortedByCountDesc()
                .stream()
                .skip((page * PAGE_SIZE) - PAGE_SIZE)
                .limit(count)
                .toList();
        List<LinkStatistic> linkStatistics = linkMapper.toLinkStatisticList(links);

        log.info("stats {}", linkStatistics);
        return linkStatistics;
    }

    private void createRank() {
        AtomicInteger rank = new AtomicInteger(RANK_START_NUMBER);
        linkCrudService.findAllSortedByCountDesc()
                .forEach(sortedLink -> {
                    linkCrudService.updateRank(sortedLink, rank.get());
                    rank.getAndIncrement();
                });
    }

    private static void checkPageFormat(Integer page, Integer count) {
        if (page <= 0) {
            throw new PageFormatException("Your page number is " + page
                                          + "! Value must be greater than zero!");
        } else if (count > PAGE_SIZE || count <= 0) {
            throw new PageFormatException("Your page size is " + count
                                          + "! Value must be between 1 and " + PAGE_SIZE + "(inclusive)!");
        }
    }

}
