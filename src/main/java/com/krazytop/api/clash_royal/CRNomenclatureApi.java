package com.krazytop.api.clash_royal;

import com.krazytop.nomenclature.clash_royal.CRAccountLevelNomenclature;
import com.krazytop.nomenclature.clash_royal.CRCardNomenclature;
import com.krazytop.nomenclature.clash_royal.CRCardRarityNomenclature;
import com.krazytop.repository.clash_royal.CRAccountLevelNomenclatureRepository;
import com.krazytop.repository.clash_royal.CRCardNomenclatureRepository;
import com.krazytop.repository.clash_royal.CRCardRarityNomenclatureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CRNomenclatureApi {

    private final CRAccountLevelNomenclatureRepository accountLevelRepository;
    private final CRCardNomenclatureRepository cardNomenclatureRepository;
    private final CRCardRarityNomenclatureRepository cardRarityNomenclatureRepository;

    @Autowired
    public CRNomenclatureApi(CRAccountLevelNomenclatureRepository accountLevelRepository, CRCardNomenclatureRepository cardNomenclatureRepository, CRCardRarityNomenclatureRepository cardRarityNomenclatureRepository) {
        this.accountLevelRepository = accountLevelRepository;
        this.cardNomenclatureRepository = cardNomenclatureRepository;
        this.cardRarityNomenclatureRepository = cardRarityNomenclatureRepository;
    }

    public CRAccountLevelNomenclature getAccountLevelNomenclature(int accountLevel) {
        return accountLevelRepository.findFirstByLevel(accountLevel);
    }

    public CRCardNomenclature getCardNomenclature(int cardId) {
        return cardNomenclatureRepository.findFirstById(cardId);
    }

    public CRCardRarityNomenclature getCardRarityNomenclature(String rarity) {
        return cardRarityNomenclatureRepository.findFirstByName(rarity);
    }

}
