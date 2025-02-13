package com.krazytop.entity.riot;

import com.krazytop.nomenclature.riot.RIOTLanguageEnum;
import com.krazytop.nomenclature.riot.RIOTRankEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "Metadata")
public class RIOTMetadataEntity {

    @Id
    private String id = "1";
    private Integer currentLOLSeason;
    private Integer currentTFTSet;
    private String currentPatch;
    private Set<String> allTFTPatches = new HashSet<>();
    private Set<String> allLOLPatches = new HashSet<>();
    @Transient
    private List<RIOTLanguageEnum> allLanguages = Arrays.stream(RIOTLanguageEnum.values()).toList();
    @Transient
    private List<RIOTRankEnum> allRanks = Arrays.stream(RIOTRankEnum.values()).toList();
}
