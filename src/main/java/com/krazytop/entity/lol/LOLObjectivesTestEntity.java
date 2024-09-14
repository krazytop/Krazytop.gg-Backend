package com.krazytop.entity.lol;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class LOLObjectivesTestEntity {

    private int baronKills;
    private int championKills;
    private int dragonKills;
    private int hordeKills;
    private int inhibitorKills;
    private int riftHeraldKills;
    private int towerKills;
}
