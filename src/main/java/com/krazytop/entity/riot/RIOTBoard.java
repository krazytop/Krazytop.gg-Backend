package com.krazytop.entity.riot;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

@Data
@Document(collection = "Board")
public class RIOTBoard {
    private String id;
    private List<String> puuids = new ArrayList<>();
    private Date updateDate;
    private String name;

    public RIOTBoard() {
        this.setId(String.valueOf(UUID.randomUUID()));
        this.setName("My Board");
        this.setUpdateDate(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()));
    }
}
