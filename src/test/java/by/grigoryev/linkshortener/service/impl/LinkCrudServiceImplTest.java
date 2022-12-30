package by.grigoryev.linkshortener.service.impl;

import by.grigoryev.linkshortener.exception.LinkDoesNotExistException;
import by.grigoryev.linkshortener.model.Link;
import by.grigoryev.linkshortener.repository.LinkRepository;
import by.grigoryev.linkshortener.service.LinkCrudService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class LinkCrudServiceImplTest {

    private static final String ORIGINAL_LINK = "https://www.noob-club.ru/index.php?frontpage;p=45";
    private static final String SHORT_LINK = "/1/noob-club";
    private static final Integer RANK_AND_COUNT = 0;


    private LinkCrudService linkCrudService;
    private LinkRepository linkRepository;

    @BeforeEach
    void setUp() {
        linkRepository = mock(LinkRepository.class);
        linkCrudService = spy(new LinkCrudServiceImpl(linkRepository));
    }

    @Test
    @DisplayName("testing save method")
    void save() {
        Link mockedLink = getMockedLink();
        doAnswer(invocationOnMock -> invocationOnMock.getArgument(0))
                .when(linkRepository)
                .save(any(Link.class));

        Link link = linkCrudService.save(ORIGINAL_LINK);

        assertEquals(mockedLink, link);
    }

    @Test
    @DisplayName("testing updateShortLink method")
    void updateShortLink() {
        Link mockedLink = getMockedLink();
        doAnswer(invocationOnMock -> invocationOnMock.getArgument(0))
                .when(linkRepository)
                .save(any(Link.class));

        Link link = linkCrudService.updateShortLink(mockedLink, SHORT_LINK);

        assertEquals(mockedLink, link);
    }

    @Test
    @DisplayName("testing if exception throws in findFirstByShortLinkOrderByIdDesc method")
    void findFirstByShortLinkOrderByIdDescThrowsException() {
        doThrow(new LinkDoesNotExistException("This link " + SHORT_LINK + " does not exist!"))
                .when(linkRepository)
                .findFirstByShortLinkOrderByIdDesc(SHORT_LINK);

        Exception exception = assertThrows(LinkDoesNotExistException.class,
                () -> linkCrudService.findFirstByShortLinkOrderByIdDesc(SHORT_LINK));
        String expectedMessage = "This link " + SHORT_LINK + " does not exist!";
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    @DisplayName("testing findFirstByShortLinkOrderByIdDesc method")
    void findFirstByShortLinkOrderByIdDesc() {
        Link mockedLink = getMockedLink();
        doReturn(Optional.of(mockedLink))
                .when(linkRepository)
                .findFirstByShortLinkOrderByIdDesc(SHORT_LINK);

        Link link = linkCrudService.findFirstByShortLinkOrderByIdDesc(SHORT_LINK);

        assertEquals(mockedLink, link);
    }

    @Test
    @DisplayName("testing updateCount method")
    void updateCount() {
        Link mockedLink = getMockedLink();
        doAnswer(invocationOnMock -> invocationOnMock.getArgument(0))
                .when(linkRepository)
                .save(any(Link.class));

        Link link = linkCrudService.updateCount(mockedLink);

        assertEquals(mockedLink, link);
    }

    @Test
    @DisplayName("testing findAllSortedByCountDesc method")
    void findAllSortedByCountDesc() {
        Link mockedLink = getMockedLink();
        doReturn(List.of(mockedLink))
                .when(linkRepository)
                .findAll();

        List<Link> links = linkCrudService.findAllSortedByCountDesc();

        assertEquals(1, links.size());
        assertEquals(mockedLink, links.get(0));
    }

    @Test
    @DisplayName("testing updateRank method")
    void updateRank() {
        Link mockedLink = getMockedLink();
        doAnswer(invocationOnMock -> invocationOnMock.getArgument(0))
                .when(linkRepository)
                .save(any(Link.class));

        Link link = linkCrudService.updateRank(mockedLink, RANK_AND_COUNT);

        assertEquals(mockedLink, link);
    }

    private Link getMockedLink() {
        return Link.builder()
                .originalLink(ORIGINAL_LINK)
                .count(RANK_AND_COUNT)
                .rank(RANK_AND_COUNT)
                .build();
    }

}