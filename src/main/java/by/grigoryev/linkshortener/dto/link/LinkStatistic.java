package by.grigoryev.linkshortener.dto.link;

public record LinkStatistic(
        String link,
        String original,
        Integer rank,
        Integer count
) {
}
