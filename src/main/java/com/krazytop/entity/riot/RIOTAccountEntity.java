package com.krazytop.entity.riot;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "Account")
public class RIOTAccountEntity {

    private String tag;
    private String name;
    private String puuid;

}
