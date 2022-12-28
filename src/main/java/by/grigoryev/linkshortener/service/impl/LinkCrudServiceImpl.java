package by.grigoryev.linkshortener.service.impl;

import by.grigoryev.linkshortener.exception.LinkDoesNotExistException;
import by.grigoryev.linkshortener.model.OriginalLink;
import by.grigoryev.linkshortener.model.ShortLink;
import by.grigoryev.linkshortener.repository.OriginalLinkRepository;
import by.grigoryev.linkshortener.repository.ShortLinkRepository;
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

    private final OriginalLinkRepository originalLinkRepository;

    private final ShortLinkRepository shortLinkRepository;

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
        ShortLink shortLink = shortLinkRepository.findFirstByLinkOrderByIdDesc(link)
                .orElseThrow(() -> new LinkDoesNotExistException("This link " + link + " does not exist!"));
        log.info("findFirstByLinkOrderByIdDesc {}", shortLink);
        return shortLink;
    }

    @Override
    public OriginalLink findById(ShortLink shortLink) {
        OriginalLink originalLink = originalLinkRepository.findById(shortLink.getOriginalId())
                .orElseThrow(() -> new LinkDoesNotExistException("Original link with id " + shortLink.getOriginalId() +
                                                                 " does not exist!"));
        log.info("findById {}", originalLink);
        return originalLink;
    }

    @Override
    public List<ShortLink> findAllSortedByCountDesc() {
        List<ShortLink> shortLinks = shortLinkRepository.findAll()
                .stream()
                .sorted(Comparator.comparing(ShortLink::getCount).reversed())
                .toList();
        log.info("findAllSortedByCountDesc {}", shortLinks);
        return shortLinks;
    }

}
