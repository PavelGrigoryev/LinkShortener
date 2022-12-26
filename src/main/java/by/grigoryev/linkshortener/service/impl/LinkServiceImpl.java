package by.grigoryev.linkshortener.service.impl;

import by.grigoryev.linkshortener.model.OriginalLink;
import by.grigoryev.linkshortener.model.ShortLink;
import by.grigoryev.linkshortener.repository.OriginalLinkRepository;
import by.grigoryev.linkshortener.repository.ShortLinkRepository;
import by.grigoryev.linkshortener.service.LinkService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class LinkServiceImpl implements LinkService {

    private final OriginalLinkRepository originalLinkRepository;

    private final ShortLinkRepository shortLinkRepository;

    @Override
    public ShortLink generate(OriginalLink originalLink) {
        save(originalLink);

        int indexOfParam = originalLink.getOriginal().lastIndexOf("=");
        String param = originalLink.getOriginal().substring(indexOfParam + 1);

        String[] splitTwoSlash = originalLink.getOriginal().split("//");
        int indexOfUrl = splitTwoSlash[1].indexOf("/");
        String siteName = splitTwoSlash[1].substring(0, indexOfUrl);

        String[] splitSiteName = siteName.split("\\.");

        String result = "/" + param + "/" + splitSiteName[1];

        ShortLink shortLink = createShortLink(result, originalLink.getId());
        save(shortLink);

        log.info("generate {}", shortLink);
        return shortLink;
    }

    @Override
    public OriginalLink redirect(String link) {
        ShortLink shortLink = shortLinkRepository.findFirstByLinkOrderByIdDesc(link)
                .orElseThrow(() -> new RuntimeException("No link"));
        OriginalLink originalLink = originalLinkRepository.findById(shortLink.getOriginalId())
                .orElseThrow(() -> new RuntimeException("No ID"));
        log.info("redirect {}", originalLink);
        return originalLink;
    }

    @Override
    public void save(OriginalLink originalLink) {
        OriginalLink link = originalLinkRepository.save(originalLink);
        log.info("save original {}", link);
    }

    @Override
    public void save(ShortLink shortLink) {
        ShortLink link = shortLinkRepository.save(shortLink);
        log.info("save link {}", link);
    }

    private static ShortLink createShortLink(String result, Long id) {
        return ShortLink.builder()
                .link(result)
                .originalId(id)
                .build();
    }

}
