package by.grigoryev.linkshortener.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ShortLink {

    private String link;

}
