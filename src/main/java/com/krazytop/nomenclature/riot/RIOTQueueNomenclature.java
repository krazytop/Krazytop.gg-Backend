package com.krazytop.nomenclature.riot;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class RIOTQueueNomenclature {

    private String id;
    private String name;
    private String description;

}
