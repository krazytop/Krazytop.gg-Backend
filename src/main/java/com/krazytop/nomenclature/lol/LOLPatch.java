package com.krazytop.nomenclature.lol;

import com.krazytop.nomenclature.riot.RIOTQueueNomenclature;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@NoArgsConstructor
@Document(collection = "Patch")
public class LOLPatch {

    public LOLPatch(String patchId, String language) {
        this.id = patchId + "_" + language;
        this.patchId = patchId;
        this.language = language;
    }

    private String id;
    private String patchId;
    private String language;
    private Integer season;
    private List<LOLChampionNomenclature> champions;
    private List<LOLItemNomenclature> items;
    private List<LOLSummonerSpellNomenclature> summonerSpells;
    private List<LOLAugmentNomenclature> augments;
    private List<LOLRuneNomenclature> runes;
    private List<RIOTQueueNomenclature> queues;//TODO voir si on peut separer les queues lol et tft
}
