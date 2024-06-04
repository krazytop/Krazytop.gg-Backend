package com.krazytop.nomenclature.tft;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "QueueNomenclature")
public class TFTQueueNomenclature {

    private String id;
    private String name;
    private String queueType;
    private String image;

}
