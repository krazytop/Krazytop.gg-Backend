package com.krazytop.entity.api_key;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.krazytop.nomenclature.GameEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "ApiKey")
@AllArgsConstructor
public class ApiKeyEntity {

    @JsonProperty("game")
    @Id private GameEnum game;
    @JsonProperty("key")
    private String key;

}
