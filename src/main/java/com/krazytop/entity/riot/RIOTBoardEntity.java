package com.krazytop.entity.riot;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.*;

@Data
@Document(collection = "Board")
public class RIOTBoardEntity {
    private String id;
    private List<String> summonerIds = new ArrayList<>();

    public RIOTBoardEntity() {
        this.setId(String.valueOf(UUID.randomUUID()));
    }
}
