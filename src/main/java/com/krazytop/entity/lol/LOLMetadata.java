package com.krazytop.entity.lol;

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
public class LOLMetadata {

    @Id
    private Integer currentSeason;
    private String currentPatch;
    private Set<String> allPatches = new HashSet<>();
}
