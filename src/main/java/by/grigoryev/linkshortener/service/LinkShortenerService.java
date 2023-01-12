package by.grigoryev.linkshortener.service;

import by.grigoryev.linkshortener.dto.link.LinkStatistic;
import by.grigoryev.linkshortener.dto.link.OriginalLink;
import by.grigoryev.linkshortener.dto.link.ShortLink;

import java.util.List;

public interface LinkShortenerService {

    ShortLink generate(OriginalLink originalLink);

    OriginalLink redirect(String shortLink);

    LinkStatistic stat(String shortLink);

    List<LinkStatistic> stats(Integer page, Integer count);

}
