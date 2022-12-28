package by.grigoryev.linkshortener.service;

import by.grigoryev.linkshortener.dto.LinkStatistic;
import by.grigoryev.linkshortener.model.OriginalLink;
import by.grigoryev.linkshortener.model.ShortLink;

public interface LinkShortenerService {

    ShortLink generate(OriginalLink originalLink);

    OriginalLink redirect(String link);

    LinkStatistic stats(String link);

}
