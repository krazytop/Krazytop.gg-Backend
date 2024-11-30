package com.krazytop.entity.tft;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "Version")
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
public class TFTVersionEntity {

    @Id private String id = "1";
    private String version;

    public TFTVersionEntity(String version) {
        this.version = version;
    }
}
