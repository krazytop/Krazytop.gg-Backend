package com.krazytop.nomenclature.destiny;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Map;

@Data
@Document(collection = "ClassNomenclature")
public class DestinyClassNomenclature {

    @Id private Long hash;
    private Map<Long, String> nameByGender;

}
