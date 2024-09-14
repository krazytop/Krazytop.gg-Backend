package com.krazytop.entity.lol;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;

import java.util.function.IntConsumer;

@Data
public class LOLObjectivesEntity {

    private int baronKills;
    private int championKills;
    private int dragonKills;
    private int hordeKills;
    private int inhibitorKills;
    private int riftHeraldKills;
    private int towerKills;

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
