package by.grigoryev.linkshortener.service;

import by.grigoryev.linkshortener.model.OriginalLink;
import by.grigoryev.linkshortener.model.ShortLink;

import java.util.List;

public interface LinkCrudService {

    OriginalLink save(OriginalLink originalLink);

    void save(ShortLink shortLink);

    void updateCount(ShortLink link);

    ShortLink findFirstByLinkOrderByIdDesc(String link);

    OriginalLink findById(Long originalId);

    List<ShortLink> findAllSortedByCountDesc();

}
