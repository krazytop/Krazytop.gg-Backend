package com.krazytop.entity.tft;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "Metadata")
public class TFTMetadata {

    @Id
    private Integer currentSet;
    private String currentPatch;
    private Set<String> allPatches = new HashSet<>();
}
