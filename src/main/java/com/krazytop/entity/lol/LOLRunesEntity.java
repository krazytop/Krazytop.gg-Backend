package com.krazytop.entity.lol;

import com.krazytop.nomenclature.lol.LOLRuneNomenclature;
import lombok.Data;

@Data
public class LOLRunesEntity {

    private LOLRuneNomenclature primaryRuneCategory;
    private LOLRuneNomenclature primaryRuneFirstPerk;
    private LOLRuneNomenclature primaryRuneSecondPerk;
    private LOLRuneNomenclature primaryRuneThirdPerk;

    private LOLRuneNomenclature secondaryRuneCategory;
    private LOLRuneNomenclature secondaryRuneFirstPerk;
    private LOLRuneNomenclature secondaryRuneSecondaryPerk;

}
