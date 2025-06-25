package com.krazytop.nomenclature.tft;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public enum TFTQueueEnum {
    NORMAL("normal", List.of("1090", "1170")),
    RANKED("ranked", List.of("1100")),
    HYPER_ROLL("hyper-roll", List.of("1130")),
    DOUBLE_UP("double-up", List.of("1160")),
    ALL_QUEUES("all-queues", List.of());

    private final String name;
    private final List<String> ids;

    public static TFTQueueEnum fromName(String name) {
        for (TFTQueueEnum queue : TFTQueueEnum.values()) {
            if (queue.getName().equals(name)) {
                return queue;
            }
        }
        return ALL_QUEUES;
    }

}
