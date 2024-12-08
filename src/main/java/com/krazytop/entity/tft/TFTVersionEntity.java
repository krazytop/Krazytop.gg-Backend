package com.krazytop.entity.tft;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "Version")
@JsonIgnoreProperties(ignoreUnknown = true)
public class TFTVersionEntity {

    @Id private String id = "1";
    private String communityVersion;
    private String officialVersion;
    private Integer currentSet;

    public TFTVersionEntity(String communityVersion, String officialVersion, Integer currentSet) {
        this.communityVersion = communityVersion;
        this.officialVersion = officialVersion;
        this.currentSet = currentSet;
    }
}