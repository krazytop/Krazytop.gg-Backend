package com.krazytop.entity.api_key;

import com.krazytop.nomenclature.GameEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "ApiKey")
@AllArgsConstructor
public class ApiKeyEntity {

    @Id private GameEnum game;
    private String key;

}
