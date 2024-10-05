package com.krazytop.service.clash_royal;

import com.krazytop.nomenclature.clash_royal.CRAccountLevelNomenclature;
import com.krazytop.nomenclature.clash_royal.CRArenaNomenclature;
import com.krazytop.nomenclature.clash_royal.CRCardNomenclature;
import com.krazytop.nomenclature.clash_royal.CRCardRarityNomenclature;
import com.krazytop.repository.clash_royal.CRAccountLevelNomenclatureRepository;
import com.krazytop.repository.clash_royal.CRArenaNomenclatureRepository;
import com.krazytop.repository.clash_royal.CRCardNomenclatureRepository;
import com.krazytop.repository.clash_royal.CRCardRarityNomenclatureRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedConstruction;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CRNomenclatureServiceTest {

    @InjectMocks
    private CRNomenclatureService nomenclatureService;
    @Mock
    private CRCardRarityNomenclatureRepository cardRarityNomenclatureRepository;
    @Mock
    private CRAccountLevelNomenclatureRepository accountLevelNomenclatureRepository;
    @Mock
    private CRCardNomenclatureRepository cardNomenclatureRepository;
    @Mock
    private CRArenaNomenclatureRepository arenaNomenclatureRepository;

    @Test
    void testUpdateAllNomenclature() {
        AtomicInteger urlConstructorCount = new AtomicInteger();
        try (MockedConstruction<URI> uriMock = mockConstruction(URI.class, (urlConstructor, context) -> {
            int urlConstructorCountInt = urlConstructorCount.getAndIncrement();
            String nomenclature = urlConstructorCountInt == 0 ? "card-rarities" : urlConstructorCountInt == 1 ? "cards" : urlConstructorCountInt == 2 ? "account-levels" : "arenas";
            when(urlConstructor.toURL()).thenReturn(getJson(nomenclature));
        })) {

            ArgumentCaptor<List<CRCardRarityNomenclature>> cardRarityArgumentCaptor = ArgumentCaptor.forClass(List.class);
            ArgumentCaptor<List<CRCardNomenclature>> cardArgumentCaptor = ArgumentCaptor.forClass(List.class);
            ArgumentCaptor<List<CRAccountLevelNomenclature>> accountLevelArgumentCaptor = ArgumentCaptor.forClass(List.class);
            ArgumentCaptor<List<CRArenaNomenclature>> arenaArgumentCaptor = ArgumentCaptor.forClass(List.class);

            assertDoesNotThrow(() -> nomenclatureService.updateAllNomenclatures());

            assertEquals(4, uriMock.constructed().size());
            verifyCardRarity(cardRarityArgumentCaptor);
            verifyCard(cardArgumentCaptor);
            verifyAccountLevel(accountLevelArgumentCaptor);
            verifyArena(arenaArgumentCaptor);
        }
    }

    private void verifyCardRarity(ArgumentCaptor<List<CRCardRarityNomenclature>> argumentCaptor) {
        verify(cardRarityNomenclatureRepository, times(1)).saveAll(argumentCaptor.capture());
        assertFalse(argumentCaptor.getValue().isEmpty());
        CRCardRarityNomenclature nomenclature = argumentCaptor.getValue().get(0);
        assertEquals("Common", nomenclature.getName());
        assertEquals(1, nomenclature.getRelativeLevel());
        assertEquals(List.of(5000, 50000), nomenclature.getUpgradeCost());
    }

    private void verifyCard(ArgumentCaptor<List<CRCardNomenclature>> argumentCaptor) {
        verify(cardNomenclatureRepository, times(1)).saveAll(argumentCaptor.capture());
        assertFalse(argumentCaptor.getValue().isEmpty());
        CRCardNomenclature nomenclature = argumentCaptor.getValue().get(0);
        assertEquals(26000000, nomenclature.getId());
        assertEquals("Common", nomenclature.getRarity());
        assertEquals("knight.png", nomenclature.getImage());
        assertEquals(3, nomenclature.getElixir());
        assertEquals("Chevalier", nomenclature.getName());
        assertEquals("Un spécialiste du combat rapproché, cousin charmant et cultivé du barbare. On raconte que la magnificence de sa moustache a suffi à justifier son adoubement.", nomenclature.getDescription());
        assertEquals("Troop", nomenclature.getType());
    }

    private void verifyAccountLevel(ArgumentCaptor<List<CRAccountLevelNomenclature>> argumentCaptor) {
        verify(accountLevelNomenclatureRepository, times(1)).saveAll(argumentCaptor.capture());
        assertFalse(argumentCaptor.getValue().isEmpty());
        CRAccountLevelNomenclature nomenclature = argumentCaptor.getValue().get(0);
        assertEquals(70, nomenclature.getLevel());
        assertEquals(16, nomenclature.getTowerLevel());
        assertEquals(15, nomenclature.getSummonerLevel());
        assertEquals(525000, nomenclature.getExpToNextLevel());
    }

    private void verifyArena(ArgumentCaptor<List<CRArenaNomenclature>> argumentCaptor) {
        verify(arenaNomenclatureRepository, times(1)).saveAll(argumentCaptor.capture());
        assertFalse(argumentCaptor.getValue().isEmpty());
        CRArenaNomenclature nomenclature = argumentCaptor.getValue().get(0);
        assertEquals(54000055, nomenclature.getId());
        assertEquals("Arena 13", nomenclature.getName());
        assertEquals("arena13.png", nomenclature.getImage());
    }

    private URL getJson(String nomenclature) {
        try {
            return new File(String.format("%s/src/test/resources/clash-royal/nomenclature/%s.json", System.getProperty("user.dir"), nomenclature)).toURI().toURL();
        } catch (MalformedURLException e) {
            return null;
        }
    }

}
