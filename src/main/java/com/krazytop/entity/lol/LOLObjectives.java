package com.krazytop.entity.lol;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;

import java.util.function.IntConsumer;

@Data
public class LOLObjectives {

    private Integer baronKills;
    private Integer championKills;
    private Integer dragonKills;
    private Integer hordeKills;
    private Integer inhibitorKills;
    private Integer riftHeraldKills;
    private Integer towerKills;
    private Integer atakhanKills;

    private void getKills(JsonNode node, IntConsumer setter) {
        setter.accept(node.get("kills").asInt());
    }

    @JsonProperty("baron")
    private void unpackBaron(JsonNode node) {
        this.getKills(node, this::setBaronKills);
    }

    @JsonProperty("champion")
    private void unpackChampion(JsonNode node) {
        this.getKills(node, this::setChampionKills);
    }

    @JsonProperty("dragon")
    private void unpackDragon(JsonNode node) {
        this.getKills(node, this::setDragonKills);
    }

    @JsonProperty("horde")
    private void unpackHorde(JsonNode node) {
        this.getKills(node, this::setHordeKills);
    }

    @JsonProperty("atakhan")
    private void unpackAtakhan(JsonNode node) {
        this.getKills(node, this::setAtakhanKills);
    }

    @JsonProperty("inhibitor")
    private void unpackInhibitor(JsonNode node) {
        this.getKills(node, this::setInhibitorKills);
    }

    @JsonProperty("riftHerald")
    private void unpackRiftHerald(JsonNode node) {
        this.getKills(node, this::setRiftHeraldKills);
    }

    @JsonProperty("tower")
    private void unpackTower(JsonNode node) {
        this.getKills(node, this::setTowerKills);
    }
}
