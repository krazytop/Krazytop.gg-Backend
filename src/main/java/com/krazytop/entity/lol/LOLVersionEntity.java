package com.krazytop.entity.lol;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "Version")
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
public class LOLVersionEntity {

    @Id private String id = "1";
    private String item;
    private String rune;
    private String mastery;
    private String summoner;
    private String champion;
    private String profileicon;
    private String language;
    private String sticker;
    private String augment;
    private Integer currentSeason;

    public LOLVersionEntity(String version) {
        this.item = version;
        this.rune = version;
        this.mastery = version;
        this.summoner = version;
        this.champion = version;
        this.profileicon = version;
        this.language = version;
        this.sticker = version;
        this.augment = version;
        this.currentSeason = 1;
    }
}
