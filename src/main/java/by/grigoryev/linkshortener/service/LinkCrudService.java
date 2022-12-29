package by.grigoryev.linkshortener.service;

import by.grigoryev.linkshortener.model.Link;

import java.util.List;

public interface LinkCrudService {

    Link save(String originalLink);

    Link updateShortLink(Link link, String shortLink);

    Link findFirstByShortLinkOrderByIdDesc(String shortLink);

    Link updateCount(Link link);

    List<Link> findAllSortedByCountDesc();

    Link updateRank(Link link, Integer rank);

}
