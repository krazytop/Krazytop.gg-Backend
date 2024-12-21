package com.krazytop.nomenclature.tft;

import com.krazytop.nomenclature.riot.RIOTRankEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;

@Getter
@AllArgsConstructor
public enum TFTQueueEnum {
    NORMAL("normal", List.of(1090, 1170), null),
    RANKED("ranked", List.of(1100), RIOTRankEnum.RANKED_TFT),
    HYPER_ROLL("hyper-roll", List.of(1130), RIOTRankEnum.RANKED_TFT_TURBO),
    DOUBLE_UP("double-up", List.of(1160), RIOTRankEnum.RANKED_TFT_DOUBLE_UP),
    ALL_QUEUES("all-queues", List.of(), null);

    private final String name;
    private final List<Integer> ids;
    private final RIOTRankEnum rank;

    public static TFTQueueEnum fromName(String name) {
        for (TFTQueueEnum queue : TFTQueueEnum.values()) {
            if (queue.getName().equals(name)) {
                return queue;
            }
        }
        return ALL_QUEUES;
    }

    public static List<TFTQueueEnum> getAllRankedQueues() {
        return Arrays.stream(TFTQueueEnum.values()).filter(queue -> queue.rank != null).toList();
    }
}
