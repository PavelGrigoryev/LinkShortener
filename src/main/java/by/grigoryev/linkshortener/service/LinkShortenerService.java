package by.grigoryev.linkshortener.service;

import by.grigoryev.linkshortener.dto.LinkStatistic;
import by.grigoryev.linkshortener.dto.OriginalLink;
import by.grigoryev.linkshortener.dto.ShortLink;

import java.util.List;

public interface LinkShortenerService {

    ShortLink generate(OriginalLink originalLink);

    OriginalLink redirect(String shortLink);

    LinkStatistic stat(String shortLink);

    List<LinkStatistic> stats(Integer page, Integer count);

}
