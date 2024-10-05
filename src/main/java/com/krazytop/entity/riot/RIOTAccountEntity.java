package com.krazytop.entity.riot;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "Account")
public class RIOTAccountEntity {

    @JsonAlias("tagLine")
    @JsonProperty("tag")
    private String tag;
    @JsonAlias("gameName")
    @JsonProperty("name")
    private String name;
    @JsonProperty("puuid")
    private String puuid;

}
