package com.krazytop.entity.lol;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.krazytop.nomenclature.lol.LOLQueueNomenclature;
import com.krazytop.repository.lol.LOLQueueNomenclatureRepository;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.stereotype.Service;

import java.util.List;

@Data
@Document(collection = "Match")
@JsonIgnoreProperties(ignoreUnknown = true)
public class LOLMatchTestEntity {

    private String id;
    @JsonProperty("gameVersion")
    private String version;
    @JsonProperty("gameCreation")
    private Long datetime;
    @JsonProperty("gameDuration")
    private Long duration;
    private LOLQueueNomenclature queue;
    private List<LOLTeamTestEntity> teams;
    private boolean remake;

    @JsonProperty("queueId")
    private void unpackQueue(JsonNode node) {
        LOLQueueNomenclatureRepository queueNomenclatureRepository = new Test().getQueueNomenclatureRepository();
        System.out.println(queueNomenclatureRepository);
    }

}

@Service
@Data
class Test {

    @Autowired private LOLQueueNomenclatureRepository queueNomenclatureRepository;

}