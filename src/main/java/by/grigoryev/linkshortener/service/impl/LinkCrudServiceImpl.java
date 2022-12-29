package by.grigoryev.linkshortener.service.impl;

import by.grigoryev.linkshortener.exception.LinkDoesNotExistException;
import by.grigoryev.linkshortener.model.Link;
import by.grigoryev.linkshortener.repository.LinkRepository;
import by.grigoryev.linkshortener.service.LinkCrudService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class LinkCrudServiceImpl implements LinkCrudService {

    private static final int NUMBER_OF_REDIRECTS_AND_RATING_WHEN_CREATING_A_LINK = 0;
    private static final int REDIRECT_LINK_INCREMENT = 1;

    private final LinkRepository linkRepository;

    @Override
    public Link save(String originalLink) {
        Link link = createLink(originalLink);
        linkRepository.save(link);
        log.info("save {}", link);
        return link;
    }

    @Override
    public Link updateShortLink(Link link, String shortLink) {
        link.setShortLink(shortLink);
        Link updatedLink = linkRepository.save(link);
        log.info("updateShortLink {}", updatedLink);
        return updatedLink;
    }

    @Override
    public Link findFirstByShortLinkOrderByIdDesc(String shortLink) {
        Link link = linkRepository.findFirstByShortLinkOrderByIdDesc(shortLink)
                .orElseThrow(() -> new LinkDoesNotExistException("This link " + shortLink + " does not exist!"));
        log.info("findFirstByShortLinkOrderByIdDesc {}", link);
        return link;
    }

    @Override
    public Link updateCount(Link link) {
        link.setCount(link.getCount() + REDIRECT_LINK_INCREMENT);
        Link updatedLink = linkRepository.save(link);
        log.info("updateCount {}", updatedLink);
        return updatedLink;
    }

    @Override
    public List<Link> findAllSortedByCountDesc() {
        List<Link> shortLinks = linkRepository.findAll()
                .stream()
                .sorted(Comparator.comparing(Link::getCount).reversed())
                .toList();
        log.info("findAllSortedByCountDesc {}", shortLinks);
        return shortLinks;
    }

    @Override
    public Link updateRank(Link link, Integer rank) {
        link.setRank(rank);
        Link updatedLink = linkRepository.save(link);
        log.info("updateRank {}", updatedLink);
        return updatedLink;
    }

    private Link createLink(String originalLink) {
        return Link.builder()
                .originalLink(originalLink)
                .count(NUMBER_OF_REDIRECTS_AND_RATING_WHEN_CREATING_A_LINK)
                .rank(NUMBER_OF_REDIRECTS_AND_RATING_WHEN_CREATING_A_LINK)
                .build();
    }

}
