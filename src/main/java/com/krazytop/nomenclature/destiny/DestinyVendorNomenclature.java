package com.krazytop.nomenclature.destiny;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "VendorNomenclature")
public class DestinyVendorNomenclature {

    @Id private Long hash;
    private String name;
    private String icon;
    private String iconBackground;
}
