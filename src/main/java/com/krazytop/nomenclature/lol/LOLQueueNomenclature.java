package com.krazytop.nomenclature.lol;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "QueueNomenclature")
public class LOLQueueNomenclature {

    @JsonProperty("queueId")
    private String id;
    @JsonProperty("description")
    private String name;
    private String map;
    private String notes;

}
