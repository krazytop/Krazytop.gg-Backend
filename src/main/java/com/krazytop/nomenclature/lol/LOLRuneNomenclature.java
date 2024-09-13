package com.krazytop.nomenclature.lol;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.mongodb.core.mapping.Document;

@EqualsAndHashCode(callSuper = true)
@Data
@Document(collection = "RuneNomenclature")
public class LOLRuneNomenclature extends LOLNomenclature {

    private String longDescription;

}
