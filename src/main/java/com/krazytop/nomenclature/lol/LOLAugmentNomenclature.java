package com.krazytop.nomenclature.lol;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.mongodb.core.mapping.Document;

@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Document(collection = "AugmentNomenclature")
public class LOLAugmentNomenclature extends LOLNomenclature {

}
