package com.krazytop.entity.riot;

import com.krazytop.nomenclature.riot.RIOTRankEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Arrays;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "Metadata")
public class RIOTMetadataEntity {

    @Id
    private String id = "1";
    private Integer currentLOLSeason;
    private Integer currentTFTSet;
    @Transient
    private List<RIOTRankEnum> ranks = Arrays.stream(RIOTRankEnum.values()).toList();
}
