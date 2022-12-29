package by.grigoryev.linkshortener.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LinkStatistic {

    private String link;

    private String original;

    private Integer rank;

    private Integer count;

}
