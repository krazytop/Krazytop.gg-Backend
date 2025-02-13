package com.krazytop.nomenclature.lol;

import com.krazytop.nomenclature.riot.RIOTRankEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public enum LOLQueueEnum {
    NORMAL("normal", List.of("14", "61", "400", "2", "430", "490"), null),
    SOLO_RANKED("solo-ranked", List.of("4", "420"), RIOTRankEnum.RANKED_SOLO),
    FLEX_RANKED("flex-ranked", List.of("6", "42", "440"), RIOTRankEnum.RANKED_TEAM),
    ARAM("aram", List.of("65", "100", "450"), null),
    URF("urf", List.of("76", "1900", "318", "1010"), null),
    NEXUS_BLITZ("nexus-blitz", List.of("1200", "1300"), null),
    ONE_FOR_ALL("one-for-all", List.of("70", "1020"), null),
    ULTIMATE_SPELLBOOK("ultimate-spellbook", List.of("1400"), null),
    ARENA("arena", List.of("1700", "1710"), null),
    ALL_QUEUES("all-queues", List.of(), null);

    private final String name;
    private final List<String> ids;
    private final RIOTRankEnum rank;

    public static LOLQueueEnum fromName(String name) {
        for (LOLQueueEnum queue : LOLQueueEnum.values()) {
            if (queue.getName().equals(name)) {
                return queue;
            }
        }
        return ALL_QUEUES;
    }
}
