package com.krazytop.nomenclature.destiny;

import com.krazytop.entity.destiny.DestinyProgressionStepEntity;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document(collection = "ProgressionNomenclature")
public class DestinyProgressionNomenclature {

    @Id private Long hash;
    private String name;
    private String description;
    private String icon;
    private boolean repeatLastStep;
    private List<DestinyProgressionStepEntity> steps;
}
