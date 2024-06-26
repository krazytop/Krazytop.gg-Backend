package com.krazytop.entity.clash_royal;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "ApiKey")
public class CRApiKeyEntity {

    private String key;

    public CRApiKeyEntity(String key){
        this.key = key;
    }

    public String toString() {
        return this.key;
    }
}
