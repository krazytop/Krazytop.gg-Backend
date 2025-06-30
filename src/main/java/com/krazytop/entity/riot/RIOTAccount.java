package com.krazytop.entity.riot;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "Account")
public class RIOTAccount {

    @JsonAlias("tagLine")
    private String tag;
    @JsonAlias("gameName")
    private String name;
    private String puuid;

}
