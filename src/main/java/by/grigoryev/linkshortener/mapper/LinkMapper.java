package by.grigoryev.linkshortener.mapper;

import by.grigoryev.linkshortener.dto.link.LinkStatistic;
import by.grigoryev.linkshortener.dto.link.OriginalLink;
import by.grigoryev.linkshortener.dto.link.ShortLink;
import by.grigoryev.linkshortener.model.Link;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper()
public interface LinkMapper {

    @Mapping(target = "original", source = "originalLink")
    @Mapping(target = "link", source = "shortLink")
    LinkStatistic toLinkStatistic(Link link);

    List<LinkStatistic> toLinkStatisticList(List<Link> links);

    @Mapping(target = "link", source = "shortLink")
    ShortLink toShortLink(Link link);

    @Mapping(target = "original", source = "originalLink")
    OriginalLink toOriginalLink(Link link);

}
