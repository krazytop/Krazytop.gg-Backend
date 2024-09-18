package com.krazytop.entity.lol;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "Version")
@JsonIgnoreProperties(ignoreUnknown = true)
public class LOLVersionEntity {

    private String id = "1";
    private String item;
    private String rune;
    private String mastery;
    private String summoner;
    private String champion;
    private String profileicon;
    private String language;
    private String sticker;
}
