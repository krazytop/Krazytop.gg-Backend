package com.krazytop.nomenclature.tft;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@NoArgsConstructor
@Document(collection = "PatchNomenclature")
public class TFTPatchNomenclature {

    public TFTPatchNomenclature(String patchId, String language) {
        this.id = patchId + "_" + language;
        this.patchId = patchId;
        this.language = language;
    }

    private String id;
    private String patchId;
    private String language;
    private Integer set;
    private List<TFTUnitNomenclature> units;
    private List<TFTTraitNomenclature> traits;
    private List<TFTItemNomenclature> items;
    private List<TFTItemNomenclature> augments;
    private List<TFTQueueNomenclature> queues;
}
