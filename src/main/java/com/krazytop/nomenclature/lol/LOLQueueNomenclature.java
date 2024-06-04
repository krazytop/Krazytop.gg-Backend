package com.krazytop.nomenclature.lol;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "QueueNomenclature")
public class LOLQueueNomenclature {

    private String id;
    private String name;
    private String image;

}
