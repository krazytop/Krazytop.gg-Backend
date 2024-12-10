package com.krazytop.entity.riot;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "Metadata")
public class RIOTMetadataEntity {

    @Id
    private String id = "1";
    private Integer currentLOLSeason;
    private Integer currentTFTSet;
}
