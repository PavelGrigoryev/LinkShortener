package by.grigoryev.linkshortener.service.impl;

import by.grigoryev.linkshortener.dto.LinkStatistic;
import by.grigoryev.linkshortener.exception.LinkDoesNotExistException;
import by.grigoryev.linkshortener.model.OriginalLink;
import by.grigoryev.linkshortener.model.ShortLink;
import by.grigoryev.linkshortener.repository.OriginalLinkRepository;
import by.grigoryev.linkshortener.repository.ShortLinkRepository;
import by.grigoryev.linkshortener.service.LinkShortenerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class LinkShortenerServiceImpl implements LinkShortenerService {

    private final OriginalLinkRepository originalLinkRepository;

    private final ShortLinkRepository shortLinkRepository;

    @Override
    public ShortLink generate(OriginalLink originalLink) {
        OriginalLink savedLink = save(originalLink);

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
        save(shortLink);

        log.info("generate {}", shortLink);
        return shortLink;
    }

    @Override
    public OriginalLink redirect(String link) {
        ShortLink shortLink = findFirstByLinkOrderByIdDesc(link);
        updateCount(shortLink);
        OriginalLink originalLink = findById(shortLink);
        log.info("redirect {}", originalLink);
        return originalLink;
    }

    @Override
    public LinkStatistic stats(String link) {
        ShortLink shortLink = findFirstByLinkOrderByIdDesc(link);
        OriginalLink originalLink = findById(shortLink);

        List<ShortLink> shortLinks = shortLinkRepository.findAll()
                .stream()
                .sorted(Comparator.comparing(ShortLink::getCount).reversed())
                .toList();

        long rank = 0;
        for(int i = 0; i < shortLinks.size(); i++) {
            if (shortLink.equals(shortLinks.get(i))) {
                rank = i + 1L;
            }
        }

        LinkStatistic linkStatistic = createLinkStatistic(shortLink, originalLink, rank);

        log.info("stats {}", linkStatistic);
        return linkStatistic;
    }

    @Override
    public OriginalLink save(OriginalLink originalLink) {
        OriginalLink link = originalLinkRepository.save(originalLink);
        log.info("save original {}", link);
        return link;
    }

    @Override
    public void save(ShortLink shortLink) {
        ShortLink link = shortLinkRepository.save(shortLink);
        log.info("save link {}", link);
    }

    @Override
    public void updateCount(ShortLink link) {
        link.setCount(link.getCount() + 1);
        ShortLink updatedLink = shortLinkRepository.save(link);
        log.info("updateCount {}", updatedLink);
    }

    @Override
    public ShortLink findFirstByLinkOrderByIdDesc(String link) {
        return shortLinkRepository.findFirstByLinkOrderByIdDesc(link)
                .orElseThrow(() -> new LinkDoesNotExistException("This link " + link + " does not exist!"));
    }

    @Override
    public OriginalLink findById(ShortLink shortLink) {
        return originalLinkRepository.findById(shortLink.getOriginalId())
                .orElseThrow(() -> new LinkDoesNotExistException("Original link with id " + shortLink.getOriginalId() +
                                                                 " does not exist!"));
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
