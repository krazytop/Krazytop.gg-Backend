package com.krazytop.nomenclature.tft;

import com.krazytop.nomenclature.riot.RIOTQueueNomenclature;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@NoArgsConstructor
@Document(collection = "Patch")
public class TFTPatch {

    public TFTPatch(String patchId, String language) {//TODO suppr ?
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
    private List<RIOTQueueNomenclature> queues;
}
