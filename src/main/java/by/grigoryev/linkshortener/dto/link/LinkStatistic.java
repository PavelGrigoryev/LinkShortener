package by.grigoryev.linkshortener.dto.link;

import lombok.Data;

@Data
public class LinkStatistic {

    private String link;

    private String original;

    private Integer rank;

    private Integer count;

}
