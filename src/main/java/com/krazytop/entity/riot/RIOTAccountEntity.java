package com.krazytop.entity.riot;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "Account")
public class RIOTAccountEntity {

    @JsonProperty("tagLine")
    private String tag;
    @JsonProperty("gameName")
    private String name;
    @JsonProperty("puuid")
    private String puuid;

}
