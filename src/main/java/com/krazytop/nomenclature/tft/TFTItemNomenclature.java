package com.krazytop.nomenclature.tft;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.Map;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@Document(collection = "ItemNomenclature")
public class TFTItemNomenclature {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String id;
    private String name;
    @JsonAlias("icon")
    private String image;
    @JsonAlias("desc")
    private String description;
    @JsonAlias("effects")
    private Map<String, Double> variables;
    private List<String> composition;

    @JsonProperty("apiName")
    private void unpackId(String id) {
        this.id = id;
    }
}
