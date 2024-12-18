package com.krazytop.nomenclature.tft;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@Document(collection = "QueueNomenclature")
public class TFTQueueNomenclature {

    private String id;
    private String name;
    private String description;

}
