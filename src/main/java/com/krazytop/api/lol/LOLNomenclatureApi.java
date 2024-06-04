package com.krazytop.api.lol;

import com.krazytop.nomenclature.lol.*;
import com.krazytop.repository.lol.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LOLNomenclatureApi {

    private final LOLChampionNomenclatureRepository championNomenclatureRepository;
    private final LOLItemNomenclatureRepository itemNomenclatureRepository;
    private final LOLRuneNomenclatureRepository runeNomenclatureRepository;
    private final LOLQueueNomenclatureRepository queueNomenclatureRepository;
    private final LOLSummonerSpellNomenclatureRepository summonerSpellNomenclatureRepository;

    @Autowired
    public LOLNomenclatureApi(LOLChampionNomenclatureRepository championNomenclatureRepository, LOLItemNomenclatureRepository itemNomenclatureRepository, LOLRuneNomenclatureRepository runeNomenclatureRepository, LOLQueueNomenclatureRepository queueNomenclatureRepository, LOLSummonerSpellNomenclatureRepository summonerSpellNomenclatureRepository) {
        this.championNomenclatureRepository = championNomenclatureRepository;
        this.itemNomenclatureRepository = itemNomenclatureRepository;
        this.runeNomenclatureRepository = runeNomenclatureRepository;
        this.queueNomenclatureRepository = queueNomenclatureRepository;
        this.summonerSpellNomenclatureRepository = summonerSpellNomenclatureRepository;
    }

    public LOLChampionNomenclature getChampionNomenclature(String championId) {
        return championNomenclatureRepository.findFirstById(championId);
    }

    public LOLItemNomenclature getItemNomenclature(String itemId) {
        return itemNomenclatureRepository.findFirstById(itemId);
    }

    public LOLRuneNomenclature getRuneNomenclature(String runeId) {
        return runeNomenclatureRepository.findFirstById(runeId);
    }

    public LOLQueueNomenclature getQueueNomenclature(String queueId) {
        return queueNomenclatureRepository.findFirstById(queueId);
    }

    public LOLSummonerSpellNomenclature getSummonerSpellNomenclature(String summonerSpellId) {
        return summonerSpellNomenclatureRepository.findFirstById(summonerSpellId);
    }

}
