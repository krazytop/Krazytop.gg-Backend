package com.krazytop.nomenclature.tft;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "QueueNomenclature")
public class TFTQueueNomenclature {

    private String id;
    private String queueType;
    private String name;
    private String image;

    @JsonProperty("image")
    private void unpackImage(JsonNode node) {
        this.image = node.get("full").asText();
    }

}
