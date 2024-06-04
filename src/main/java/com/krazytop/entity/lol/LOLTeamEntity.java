package com.krazytop.entity.lol;

import com.krazytop.nomenclature.lol.LOLChampionNomenclature;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class LOLTeamEntity {

    private List<LOLChampionNomenclature> bannedChampions;
    private LOLObjectivesEntity objectives;
    private boolean hasWin;
    private List<LOLParticipantEntity> participants = new ArrayList<>();
}
