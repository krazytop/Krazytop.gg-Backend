package com.krazytop.nomenclature.lol;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "QueueNomenclature")
public class LOLQueueNomenclature {

    @JsonAlias("queueId")
    private String id;
    @JsonAlias("description")
    private String name;
    private String map;
    private String notes;

}
