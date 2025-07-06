package com.krazytop.nomenclature.lol;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class LOLRunePerkNomenclature extends LOLNomenclature {

    @JsonAlias("longDesc")
    private String longDescription;

}