package com.krazytop.entity.clash_royal;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "ApiKey")
@AllArgsConstructor
public class CRApiKeyEntity {

    @Id private String key;

}
