package by.grigoryev.linkshortener.service;

import by.grigoryev.linkshortener.dto.LinkStatistic;
import by.grigoryev.linkshortener.model.OriginalLink;
import by.grigoryev.linkshortener.model.ShortLink;

public interface LinkShortenerService {

    ShortLink generate(OriginalLink originalLink);

    OriginalLink redirect(String link);

    LinkStatistic stats(String link);

    OriginalLink save(OriginalLink originalLink);

    void save(ShortLink shortLink);

    void updateCount(ShortLink link);

    ShortLink findFirstByLinkOrderByIdDesc(String link);

    OriginalLink findById(ShortLink shortLink);

}
