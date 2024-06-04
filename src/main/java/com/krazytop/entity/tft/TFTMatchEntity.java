package com.krazytop.entity.tft;

import com.krazytop.nomenclature.tft.TFTQueueNomenclature;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document(collection = "Match")
public class TFTMatchEntity {

    private String id;
    private Long datetime;
    private double length;
    private String gameType;
    private String queueId;
    private TFTQueueNomenclature queue;
    private String set;
    private List<TFTParticipantEntity> participants;

    public String toString() {
        return this.id;
    }

}
