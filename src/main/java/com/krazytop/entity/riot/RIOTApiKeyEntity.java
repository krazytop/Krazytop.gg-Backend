package com.krazytop.entity.riot;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "ApiKey")
@AllArgsConstructor
public class RIOTApiKeyEntity {

    @Id private String key;

}
