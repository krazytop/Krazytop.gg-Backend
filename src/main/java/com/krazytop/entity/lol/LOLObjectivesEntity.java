package com.krazytop.entity.lol;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LOLObjectivesEntity {

    private int baronKills;
    private int championKills;
    private int dragonKills;
    private int hordeKills;
    private int inhibitorKills;
    private int riftHeraldKills;
    private int towerKills;
}
