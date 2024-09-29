package com.krazytop.service.lol;

import com.krazytop.entity.lol.LOLVersionEntity;
import com.krazytop.nomenclature.lol.LOLChampionNomenclature;
import com.krazytop.nomenclature.lol.LOLSummonerSpellNomenclature;
import com.krazytop.repository.lol.LOLSummonerSpellNomenclatureRepository;
import com.krazytop.repository.lol.LOLVersionRepository;
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
class LOLNomenclatureServiceTest {

    @InjectMocks
    private LOLNomenclatureService nomenclatureService;
    @Mock
    private LOLVersionRepository versionRepository;
    @Mock
    private LOLSummonerSpellNomenclatureRepository summonerSpellNomenclatureRepository;

    @Test
    void testUpdateAllNomenclature_NO_NEED() {
        try (MockedConstruction<URI> uriMock = mockConstruction(URI.class, (urlConstructor, context) ->
                when(urlConstructor.toURL()).thenReturn(getJson("version")))) {

            when(versionRepository.findFirstByOrderByItemAsc()).thenReturn(new LOLVersionEntity("14.19.1"));

            assertDoesNotThrow(() -> nomenclatureService.updateAllNomenclatures());

            assertEquals(1, uriMock.constructed().size());
            verify(versionRepository, times(1)).findFirstByOrderByItemAsc();
            verify(versionRepository, times(0)).save(any());
        }
    }

    @Test
    void testUpdateAllNomenclature_SUMMONER_SPELL() {
        AtomicInteger urlConstructorCount = new AtomicInteger();
        try (MockedConstruction<URI> uriMock = mockConstruction(URI.class, (urlConstructor, context) ->
                when(urlConstructor.toURL()).thenReturn(getJson(urlConstructorCount.getAndIncrement() == 0 ? "version" : "summoner")))) {

            LOLVersionEntity version = new LOLVersionEntity("14.19.1");
            version.setSummoner("14.18.1");
            when(versionRepository.findFirstByOrderByItemAsc()).thenReturn(version);
            ArgumentCaptor<List<LOLSummonerSpellNomenclature>> argumentCaptor = ArgumentCaptor.forClass(List.class);

            assertDoesNotThrow(() -> nomenclatureService.updateAllNomenclatures());

            assertEquals(2, uriMock.constructed().size());
            verify(versionRepository, times(1)).findFirstByOrderByItemAsc();
            verify(versionRepository, times(1)).save(any());
            verify(summonerSpellNomenclatureRepository, times(1)).saveAll(argumentCaptor.capture());
            assertFalse(argumentCaptor.getValue().isEmpty());
            LOLSummonerSpellNomenclature nomenclature = argumentCaptor.getValue().get(0);
            assertEquals("SummonerFlash", nomenclature.getId());
            assertEquals("Saut éclair", nomenclature.getName());
            assertEquals("SummonerFlash.png", nomenclature.getImage());
            assertEquals("Vous téléporte sur une courte distance vers l'emplacement de votre curseur.", nomenclature.getDescription());
            assertEquals(300, nomenclature.getCooldownBurn());
        }
    }

    @Test
    void testUpdateAllNomenclature_CHAMPION() {
        AtomicInteger urlConstructorCount = new AtomicInteger();
        try (MockedConstruction<URI> uriMock = mockConstruction(URI.class, (urlConstructor, context) ->
                when(urlConstructor.toURL()).thenReturn(getJson(urlConstructorCount.getAndIncrement() == 0 ? "version" : "champion")))) {

            LOLVersionEntity version = new LOLVersionEntity("14.19.1");
            version.setChampion("14.18.1");
            when(versionRepository.findFirstByOrderByItemAsc()).thenReturn(version);
            ArgumentCaptor<List<LOLChampionNomenclature>> argumentCaptor = ArgumentCaptor.forClass(List.class);

            assertDoesNotThrow(() -> nomenclatureService.updateAllNomenclatures());

            assertEquals(2, uriMock.constructed().size());
            verify(versionRepository, times(1)).findFirstByOrderByItemAsc();
            verify(versionRepository, times(1)).save(any());
            verify(summonerSpellNomenclatureRepository, times(1)).saveAll(argumentCaptor.capture());
            assertFalse(argumentCaptor.getValue().isEmpty());
            LOLSummonerSpellNomenclature nomenclature = argumentCaptor.getValue().get(0);
        }
    }

    private URL getJson(String nomenclature) {
        try {
            return new File(String.format("%s/src/test/resources/lol/nomenclature/%s.json", System.getProperty("user.dir"), nomenclature)).toURI().toURL();
        } catch (MalformedURLException e) {
            return null;
        }
    }

}
