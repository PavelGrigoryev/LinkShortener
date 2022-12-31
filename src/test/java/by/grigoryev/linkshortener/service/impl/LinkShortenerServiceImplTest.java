package by.grigoryev.linkshortener.service.impl;

import by.grigoryev.linkshortener.dto.LinkStatistic;
import by.grigoryev.linkshortener.dto.OriginalLink;
import by.grigoryev.linkshortener.exception.PageFormatException;
import by.grigoryev.linkshortener.mapper.LinkMapper;
import by.grigoryev.linkshortener.model.Link;
import by.grigoryev.linkshortener.service.LinkCrudService;
import by.grigoryev.linkshortener.service.LinkShortenerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class LinkShortenerServiceImplTest {

    private static final String ORIGINAL_LINK = "https://www.noob-club.ru/index.php?frontpage;p=45";
    private static final String SHORT_LINK = "/1/noob-club";
    private static final Integer RANK_AND_COUNT = 0;
    public static final int PAGE = 1;
    public static final int COUNT = 5;

    private LinkShortenerService linkShortenerService;
    private LinkCrudService linkCrudService;
    private final LinkMapper linkMapper = Mappers.getMapper(LinkMapper.class);

    @BeforeEach
    void setUp() {
        linkCrudService = mock(LinkCrudService.class);
        linkShortenerService = spy(new LinkShortenerServiceImpl(linkCrudService));
    }

    @Test
    @DisplayName("testing redirect method")
    void redirect() {
        Link mockedLink = getMockedLink();
        OriginalLink mockedOriginalLink = linkMapper.toOriginalLink(mockedLink);

        doReturn(mockedLink)
                .when(linkCrudService)
                .findFirstByShortLinkOrderByIdDesc(SHORT_LINK);

        OriginalLink originalLink = linkShortenerService.redirect(SHORT_LINK);

        assertEquals(mockedOriginalLink, originalLink);
    }

    @Test
    @DisplayName("testing stat method")
    void stat() {
        Link mockedLink = getMockedLink();
        LinkStatistic mockedLinkStatistic = linkMapper.toLinkStatistic(mockedLink);

        doReturn(mockedLink)
                .when(linkCrudService)
                .findFirstByShortLinkOrderByIdDesc(SHORT_LINK);

        LinkStatistic linkStatistic = linkShortenerService.stat(SHORT_LINK);

        assertEquals(mockedLinkStatistic, linkStatistic);
    }

    @Test
    @DisplayName("testing stats method")
    void stats() {
        List<Link> mockedLinks = List.of(getMockedLink());
        List<LinkStatistic> mockedLinkStatistics = linkMapper.toLinkStatisticList(mockedLinks);

        doReturn(mockedLinks)
                .when(linkCrudService)
                .findAllSortedByCountDesc();

        List<LinkStatistic> linkStatistics = linkShortenerService.stats(PAGE, COUNT);

        assertEquals(mockedLinkStatistics, linkStatistics);
    }

    @Test
    @DisplayName("testing if exception throws in stats method")
    void statsThrowsException() {
        doThrow(new PageFormatException("Your page number is " + PAGE + "! Value must be greater than zero!"))
                .when(linkShortenerService)
                .stats(PAGE, COUNT);

        Exception exception = assertThrows(PageFormatException.class,
                () -> linkShortenerService.stats(PAGE, COUNT));
        String expectedMessage = "Your page number is " + PAGE + "! Value must be greater than zero!";

        assertEquals(expectedMessage, exception.getMessage());
    }

    private Link getMockedLink() {
        return Link.builder()
                .originalLink(ORIGINAL_LINK)
                .shortLink(SHORT_LINK)
                .count(RANK_AND_COUNT)
                .rank(RANK_AND_COUNT)
                .build();
    }

}