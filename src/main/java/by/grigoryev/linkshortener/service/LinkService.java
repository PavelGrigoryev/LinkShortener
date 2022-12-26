package by.grigoryev.linkshortener.service;

import by.grigoryev.linkshortener.model.OriginalLink;
import by.grigoryev.linkshortener.model.ShortLink;

public interface LinkService {

    ShortLink generate(OriginalLink originalLink);

    OriginalLink redirect(String link);

    void save(OriginalLink originalLink);

    void save(ShortLink shortLink);

}
