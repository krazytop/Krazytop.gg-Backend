package com.krazytop.service.tft;

import com.krazytop.entity.tft.RIOTPatchEntity;
import com.krazytop.nomenclature.tft.TFTItemNomenclature;
import com.krazytop.nomenclature.tft.TFTQueueNomenclature;
import com.krazytop.nomenclature.tft.TFTTraitNomenclature;
import com.krazytop.nomenclature.tft.TFTUnitNomenclature;
import com.krazytop.repository.riot.RIOTPatchRepository;
import com.krazytop.service.riot.RIOTMetadataService;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TFTNomenclatureServiceTest {

    @InjectMocks
    private TFTNomenclatureService nomenclatureService;

    @Mock
    private RIOTPatchRepository versionRepository;
    @Mock
    private TFTTraitNomenclatureRepository traitNomenclatureRepository;
    @Mock
    private TFTUnitNomenclatureRepository unitNomenclatureRepository;
    @Mock
    private TFTQueueNomenclatureRepository queueNomenclatureRepository;
    @Mock
    private TFTItemNomenclatureRepository itemNomenclatureRepository;
    @Mock
    private RIOTMetadataService metadataService;

    @Test
    void testUpdateAllNomenclature_NONEED() throws IOException, URISyntaxException {
        AtomicInteger urlConstructorCount = new AtomicInteger();
        try (MockedConstruction<URI> uriMock = mockConstruction(URI.class, (urlConstructor, context) -> {
            int constructorCount = urlConstructorCount.getAndIncrement();
            when(urlConstructor.toURL()).thenReturn(getJson(constructorCount == 0 ? "official-dragon-version" : "community-dragon-version"));
        })) {

            RIOTPatchEntity version = new RIOTPatchEntity("14.23.6369832+branch.releases-14-23.content.release", "14.19.1");
            when(versionRepository.findFirstByOrderByOfficialVersionAsc()).thenReturn(version);

            assertFalse(nomenclatureService.updateAllNomenclatures());

            assertEquals(2, uriMock.constructed().size());
            verify(versionRepository, times(1)).findFirstByOrderByOfficialVersionAsc();
            verify(versionRepository, times(0)).save(any());
            verify(metadataService, times(0)).updateMetadata(any());
        }
    }

    @Test
    void testUpdateAllNomenclature_OFFICIAL() throws IOException, URISyntaxException {
        AtomicInteger urlConstructorCount = new AtomicInteger();
        try (MockedConstruction<URI> uriMock = mockConstruction(URI.class, (urlConstructor, context) -> {
                    int constructorCount = urlConstructorCount.getAndIncrement();
                    when(urlConstructor.toURL()).thenReturn(getJson(constructorCount == 0 ? "official-dragon-version" : constructorCount == 1 ? "community-dragon-version" : "official-dragon"));
                })) {

            RIOTPatchEntity version = new RIOTPatchEntity("14.23.6369832+branch.releases-14-23.content.release", "14.18.1");
            when(versionRepository.findFirstByOrderByOfficialVersionAsc()).thenReturn(version);
            ArgumentCaptor<RIOTPatchEntity> versionArgumentCaptor = ArgumentCaptor.forClass(RIOTPatchEntity.class);
            ArgumentCaptor<List<TFTQueueNomenclature>> queuesArgumentCaptor = ArgumentCaptor.forClass(List.class);

            assertTrue(nomenclatureService.updateAllNomenclatures());

            assertEquals(3, uriMock.constructed().size());
            verify(versionRepository, times(1)).findFirstByOrderByOfficialVersionAsc();
            verify(versionRepository, times(1)).save(versionArgumentCaptor.capture());
            assertEquals("14.19.1", versionArgumentCaptor.getValue().getCurrent());
            verify(queueNomenclatureRepository, times(1)).saveAll(queuesArgumentCaptor.capture());
            assertEquals(1, queuesArgumentCaptor.getValue().size());
            TFTQueueNomenclature nomenclature = queuesArgumentCaptor.getValue().get(0);
            assertEquals("1130", nomenclature.getId());
            assertEquals("TFTM_ModeIcon_Turbo.png", nomenclature.getImage());
            assertEquals("HYPER ROLL", nomenclature.getName());
            assertEquals("RANKED_TFT_TURBO", nomenclature.getQueueType());
        }
    }

    @Test
    void testUpdateAllNomenclature_COMMUNITY() throws IOException, URISyntaxException {
        AtomicInteger urlConstructorCount = new AtomicInteger();
        try (MockedConstruction<URI> uriMock = mockConstruction(URI.class, (urlConstructor, context) -> {
            int constructorCount = urlConstructorCount.getAndIncrement();
            when(urlConstructor.toURL()).thenReturn(getJson(constructorCount == 0 ? "official-dragon-version" : constructorCount == 1 ? "community-dragon-version" : "community-dragon"));
        })) {

            RIOTPatchEntity version = new RIOTPatchEntity("14.22.6369832+branch.releases-14-23.content.release", "14.19.1");
            when(versionRepository.findFirstByOrderByOfficialVersionAsc()).thenReturn(version);
            ArgumentCaptor<RIOTPatchEntity> versionArgumentCaptor = ArgumentCaptor.forClass(RIOTPatchEntity.class);
            ArgumentCaptor<List<TFTItemNomenclature>> itemsArgumentCaptor = ArgumentCaptor.forClass(List.class);
            ArgumentCaptor<List<TFTUnitNomenclature>> unitsArgumentCaptor = ArgumentCaptor.forClass(List.class);
            ArgumentCaptor<List<TFTTraitNomenclature>> traitsArgumentCaptor = ArgumentCaptor.forClass(List.class);

            assertTrue(nomenclatureService.updateAllNomenclatures());

            assertEquals(3, uriMock.constructed().size());
            verify(versionRepository, times(1)).findFirstByOrderByOfficialVersionAsc();
            verify(versionRepository, times(1)).save(versionArgumentCaptor.capture());
            assertEquals("14.23.6369832+branch.releases-14-23.content.release", versionArgumentCaptor.getValue().getAll());
            verify(metadataService, times(1)).updateMetadata(any());
            verifyItems(itemsArgumentCaptor);
            verifyUnits(unitsArgumentCaptor);
            verifyTraits(traitsArgumentCaptor);
        }
    }

    private void verifyItems(ArgumentCaptor<List<TFTItemNomenclature>> itemsArgumentCaptor) {
        verify(itemNomenclatureRepository, times(1)).saveAll(itemsArgumentCaptor.capture());
        assertEquals(1, itemsArgumentCaptor.getValue().size());
        TFTItemNomenclature nomenclature = itemsArgumentCaptor.getValue().get(0);
        assertEquals("TFT_Item_RabadonsDeathcap", nomenclature.getId());
        assertEquals("Coiffe de Rabadon", nomenclature.getName());
        assertEquals("ASSETS/Maps/TFT/Icons/Items/Hexcore/TFT_Item_RabadonsDeathcap.TFT_Set13.tex", nomenclature.getImage());
        assertEquals("Ce modeste chapeau peut vous aider à façonner ou à détruire le monde.", nomenclature.getDescription());
        assertEquals(List.of("TFT_Item_NeedlesslyLargeRod", "TFT_Item_NeedlesslyLargeRod"), nomenclature.getComposition());
        Map<String, Double> expectedVariables = new HashMap<>();
        expectedVariables.put("AP", 50.0);
        assertEquals(expectedVariables, nomenclature.getVariables());
    }

    private void verifyUnits(ArgumentCaptor<List<TFTUnitNomenclature>> unitsArgumentCaptor) {
        verify(unitNomenclatureRepository, times(1)).saveAll(unitsArgumentCaptor.capture());
        assertEquals(1, unitsArgumentCaptor.getValue().size());
        TFTUnitNomenclature nomenclature = unitsArgumentCaptor.getValue().get(0);
        assertEquals("TFT13_Tristana", nomenclature.getId());
        assertEquals("Tristana", nomenclature.getName());
        assertEquals("ASSETS/Characters/TFT13_Tristana/HUD/TFT13_Tristana_Square.TFT_Set13.tex", nomenclature.getImage());
        assertEquals(2, nomenclature.getCost());
        assertEquals(List.of("Émissaire", "Artilleur"), nomenclature.getTraits());
        TFTUnitNomenclature.TFTAbilityEntity ability = nomenclature.getAbility();
        assertEquals("Tristana tire un boulet de canon vers sa cible", ability.getDescription());
        assertEquals("ASSETS/Characters/TFT13_Tristana/HUD/Icons2D/TFT13_Tristana_Passive.TFT_Set13.tex", ability.getImage());
        assertEquals("Tir de précision", ability.getName());
        List<TFTUnitNomenclature.TFTAbilityEntity.TFTVariableEntity> variables = ability.getVariables();
        assertEquals(1, variables.size());
        TFTUnitNomenclature.TFTAbilityEntity.TFTVariableEntity variable = variables.get(0);
        assertEquals("PercentAttackDamage", variable.getName());
        assertEquals(List.of(5.25), variable.getValue());
    }

    private void verifyTraits(ArgumentCaptor<List<TFTTraitNomenclature>> traitsArgumentCaptor) {
        verify(traitNomenclatureRepository, times(1)).saveAll(traitsArgumentCaptor.capture());
        assertEquals(1, traitsArgumentCaptor.getValue().size());
        TFTTraitNomenclature nomenclature = traitsArgumentCaptor.getValue().get(0);
        assertEquals("TFT13_Bruiser", nomenclature.getId());
        assertEquals("Bagarreur", nomenclature.getName());
        assertEquals("Vos unités gagnent +@TeamFlatHealth@ PV max", nomenclature.getDescription());
        assertEquals("ASSETS/UX/TraitIcons/Trait_Icon_6_Bruiser.tex", nomenclature.getImage());
        List<TFTTraitNomenclature.TFTEffectEntity> effects = nomenclature.getEffects();
        assertEquals(1, effects.size());
        TFTTraitNomenclature.TFTEffectEntity effect = effects.get(0);
        assertEquals(3, effect.getMaxUnits());
        assertEquals(2, effect.getMinUnits());
        assertEquals(1, effect.getStyle());
        Map<String, Double> expectedVariables = new HashMap<>();
        expectedVariables.put("BonusPercentHealth", 0.20000000298023224);
        assertEquals(expectedVariables, effect.getVariables());
    }

    private URL getJson(String nomenclature) {
        try {
            return new File(String.format("%s/src/test/resources/tft/nomenclature/%s.json", System.getProperty("user.dir"), nomenclature)).toURI().toURL();
        } catch (MalformedURLException e) {
            return null;
        }
    }

}
