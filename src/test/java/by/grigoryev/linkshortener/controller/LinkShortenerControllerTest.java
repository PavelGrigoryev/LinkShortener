package by.grigoryev.linkshortener.controller;

import by.grigoryev.linkshortener.dto.link.LinkStatistic;
import by.grigoryev.linkshortener.dto.link.OriginalLink;
import by.grigoryev.linkshortener.dto.link.ShortLink;
import by.grigoryev.linkshortener.security.JwtService;
import by.grigoryev.linkshortener.service.LinkShortenerService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WithMockUser
@ExtendWith(SpringExtension.class)
@WebMvcTest(LinkShortenerController.class)
class LinkShortenerControllerTest {

    private static final String ORIGINAL_LINK = "https://www.noob-club.ru/index.php?frontpage;p=45";
    private static final String SHORT_LINK = "/1/noob-club";
    private static final Integer RANK_AND_COUNT = 0;
    private static final String JSON_LINK_CONTENT = """
            {
               "link": "/1/noob-club"
            }
            """;
    private static final String JSON_LINK_STATISTIC_CONTENT = """
            {
              "link": "/1/noob-club",
              "original": "https://www.noob-club.ru/index.php?frontpage;p=45",
              "rank": 0,
              "count": 0
            }
            """;
    public static final int PAGE = 1;
    public static final int COUNT = 5;

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private LinkShortenerService linkShortenerService;
    @MockBean
    private JwtService jwtService;

    @Test
    @DisplayName("testing generate endpoint")
    void generate() throws Exception {
        ShortLink mockedShortLink = new ShortLink();
        mockedShortLink.setLink(SHORT_LINK);

        doReturn(mockedShortLink)
                .when(linkShortenerService)
                .generate(any(OriginalLink.class));

        mockMvc.perform(post("/generate")
                        .with(csrf())
                        .content(JSON_LINK_CONTENT)
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().json(JSON_LINK_CONTENT));
    }

    @Test
    @DisplayName("testing generate endpoint throws 403")
    void generateThrows403() throws Exception {
        mockMvc.perform(post("/generate")
                        .with(csrf().useInvalidToken())
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("testing redirect endpoint")
    void redirect() throws Exception {
        OriginalLink mockedOriginalLink = new OriginalLink();
        mockedOriginalLink.setOriginal(ORIGINAL_LINK);

        doReturn(mockedOriginalLink)
                .when(linkShortenerService)
                .redirect(SHORT_LINK);

        mockMvc.perform(get("/?shortLink=" + SHORT_LINK))
                .andExpect(status().isPermanentRedirect())
                .andExpect(redirectedUrl(ORIGINAL_LINK));
    }

    @Test
    @DisplayName("testing stat endpoint")
    void stat() throws Exception {
        LinkStatistic mockedLinkStatistic = getMockedLinkStatistic();

        doReturn(mockedLinkStatistic)
                .when(linkShortenerService)
                .stat(SHORT_LINK);

        mockMvc.perform(get("/stat/?shortLink=" + SHORT_LINK))
                .andExpect(status().isOk())
                .andExpect(content().json(JSON_LINK_STATISTIC_CONTENT));
    }

    @Test
    @DisplayName("testing stats endpoint")
    void stats() throws Exception {
        List<LinkStatistic> mockedLinkStatistics = List.of(getMockedLinkStatistic());

        doReturn(mockedLinkStatistics)
                .when(linkShortenerService)
                .stats(PAGE, COUNT);

        mockMvc.perform(get("/stats/?page=" + PAGE + "&count=" + COUNT))
                .andExpect(status().isOk())
                .andExpect(content().json("[" + JSON_LINK_STATISTIC_CONTENT + "]"));
    }

    @Test
    @DisplayName("testing stats endpoint with empty list of elements")
    void statsWithEmptyList() throws Exception {
        doReturn(new ArrayList<>())
                .when(linkShortenerService)
                .stats(PAGE, COUNT);

        mockMvc.perform(get("/stats/?page=" + PAGE + "&count=" + COUNT))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    private static LinkStatistic getMockedLinkStatistic() {
        LinkStatistic mockedLinkStatistic = new LinkStatistic();
        mockedLinkStatistic.setLink(SHORT_LINK);
        mockedLinkStatistic.setOriginal(ORIGINAL_LINK);
        mockedLinkStatistic.setRank(RANK_AND_COUNT);
        mockedLinkStatistic.setCount(RANK_AND_COUNT);
        return mockedLinkStatistic;
    }

}