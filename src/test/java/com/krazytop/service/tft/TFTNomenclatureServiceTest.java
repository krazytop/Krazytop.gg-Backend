package com.krazytop.service.tft;

import com.krazytop.entity.lol.LOLVersionEntity;
import com.krazytop.nomenclature.lol.*;
import com.krazytop.repository.lol.*;
import com.krazytop.repository.tft.*;
import com.krazytop.service.lol.LOLNomenclatureService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedConstruction;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TFTNomenclatureServiceTest {

    @InjectMocks
    private TFTNomenclatureService nomenclatureService;

    @Mock
    private TFTVersionRepository versionRepository;
    @Mock
    private TFTTraitNomenclatureRepository traitNomenclatureRepository;
    @Mock
    private TFTUnitNomenclatureRepository unitNomenclatureRepository;
    @Mock
    private TFTQueueNomenclatureRepository queueNomenclatureRepository;
    @Mock
    private TFTItemNomenclatureRepository itemNomenclatureRepository;
/**
    @Test
    void testUpdateAllNomenclature_NO_NEED() throws IOException, URISyntaxException {
        try (MockedConstruction<URI> uriMock = mockConstruction(URI.class, (urlConstructor, context) ->
                when(urlConstructor.toURL()).thenReturn(getJson("version")))) {

            when(versionRepository.findFirstByOrderByItemAsc()).thenReturn(new LOLVersionEntity("14.19.1"));

            assertFalse(nomenclatureService.updateAllNomenclatures());

            assertEquals(1, uriMock.constructed().size());
            verify(versionRepository, times(1)).findFirstByOrderByItemAsc();
            verify(versionRepository, times(0)).save(any());
        }
    }**/

    @Test
    void testUpdateAllNomenclature_OFFICIAL() throws IOException, URISyntaxException {
        try (MockedConstruction<URI> uriMock = mockConstruction(URI.class, (urlConstructor, context) ->
                when(urlConstructor.toURL()).thenReturn(getJson("official-dragon")))) {

            LOLVersionEntity version = new LOLVersionEntity("14.19.1");
            version.setSummoner("14.18.1");
            when(versionRepository.findFirstByOrderByItemAsc()).thenReturn(version);
            ArgumentCaptor<List<LOLSummonerSpellNomenclature>> argumentCaptor = ArgumentCaptor.forClass(List.class);

            assertTrue(nomenclatureService.updateAllNomenclatures());

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

    private void verifyItem(ArgumentCaptor<List<LOLItemNomenclature>> argumentCaptor) {
        verify(itemNomenclatureRepository, times(1)).saveAll(argumentCaptor.capture());
        assertFalse(argumentCaptor.getValue().isEmpty());
        LOLItemNomenclature nomenclature = argumentCaptor.getValue().get(0);
        assertEquals("1001", nomenclature.getId());
        assertEquals("Bottes", nomenclature.getName());
        assertEquals("1001.png", nomenclature.getImage());
        assertEquals("<mainText><stats><attention>+25</attention> vitesse de déplacement</stats><br><br></mainText>", nomenclature.getDescription());
        assertEquals("Augmente légèrement la vitesse de déplacement.", nomenclature.getPlainText());
        assertEquals(300, nomenclature.getBaseGold());
        assertEquals(301, nomenclature.getTotalGold());
        assertEquals(1, nomenclature.getStats().size());
        assertEquals(25, nomenclature.getStats().get("FlatMovementSpeedMod"));
        assertEquals(List.of("Boots"), nomenclature.getTags());
        assertEquals(List.of("3005"), nomenclature.getFromItems());
        assertEquals(List.of("3158"), nomenclature.getToItems());
    }

    private void verifyQueue(ArgumentCaptor<List<LOLQueueNomenclature>> argumentCaptor) {
        verify(queueNomenclatureRepository, times(1)).saveAll(argumentCaptor.capture());
        assertFalse(argumentCaptor.getValue().isEmpty());
        LOLQueueNomenclature nomenclature = argumentCaptor.getValue().get(0);
        assertEquals("420", nomenclature.getId());
        assertEquals("5v5 Ranked Solo games", nomenclature.getName());
        assertEquals("Summoner's Rift", nomenclature.getMap());
        assertNull(nomenclature.getNotes());
    }

    private URL getJson(String nomenclature) {
        try {
            return new File(String.format("%s/src/test/resources/tft/nomenclature/%s.json", System.getProperty("user.dir"), nomenclature)).toURI().toURL();
        } catch (MalformedURLException e) {
            return null;
        }
    }

}
