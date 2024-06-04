package com.krazytop.entity.riot;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "ApiKey")
public class RIOTApiKeyEntity {

    private String key;

}
