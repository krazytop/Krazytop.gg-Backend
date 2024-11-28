package com.krazytop.service.lol;

import com.krazytop.entity.lol.LOLVersionEntity;
import com.krazytop.nomenclature.lol.*;
import com.krazytop.repository.lol.*;
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
class LOLNomenclatureServiceTest {

    @InjectMocks
    private LOLNomenclatureService nomenclatureService;

    @Mock
    private LOLVersionRepository versionRepository;
    @Mock
    private LOLSummonerSpellNomenclatureRepository summonerSpellNomenclatureRepository;
    @Mock
    private LOLChampionNomenclatureRepository championNomenclatureRepository;
    @Mock
    private LOLItemNomenclatureRepository itemNomenclatureRepository;
    @Mock
    private LOLRuneNomenclatureRepository runeNomenclatureRepository;
    @Mock
    private LOLQueueNomenclatureRepository queueNomenclatureRepository;
    @Mock
    private LOLAugmentNomenclatureRepository augmentNomenclatureRepository;

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
    }

    @Test
    void testUpdateAllNomenclature_SUMMONER_SPELL() throws IOException, URISyntaxException {
        AtomicInteger urlConstructorCount = new AtomicInteger();
        try (MockedConstruction<URI> uriMock = mockConstruction(URI.class, (urlConstructor, context) ->
                when(urlConstructor.toURL()).thenReturn(getJson(urlConstructorCount.getAndIncrement() == 0 ? "version" : "summoner")))) {

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

    @Test
    void testUpdateAllNomenclature_CHAMPION() throws IOException, URISyntaxException {
        AtomicInteger urlConstructorCount = new AtomicInteger();
        try (MockedConstruction<URI> uriMock = mockConstruction(URI.class, (urlConstructor, context) ->
                when(urlConstructor.toURL()).thenReturn(getJson(urlConstructorCount.getAndIncrement() == 0 ? "version" : "champion")))) {

            LOLVersionEntity version = new LOLVersionEntity("14.19.1");
            version.setChampion("14.18.1");
            when(versionRepository.findFirstByOrderByItemAsc()).thenReturn(version);
            ArgumentCaptor<List<LOLChampionNomenclature>> argumentCaptor = ArgumentCaptor.forClass(List.class);

            assertTrue(nomenclatureService.updateAllNomenclatures());

            assertEquals(2, uriMock.constructed().size());
            verify(versionRepository, times(1)).findFirstByOrderByItemAsc();
            verify(versionRepository, times(1)).save(any());
            verify(championNomenclatureRepository, times(1)).saveAll(argumentCaptor.capture());
            assertFalse(argumentCaptor.getValue().isEmpty());
            LOLChampionNomenclature nomenclature = argumentCaptor.getValue().get(0);
            assertEquals("266", nomenclature.getId());
            assertEquals("Aatrox", nomenclature.getName());
            assertEquals("Aatrox.png", nomenclature.getImage());
            assertEquals("Autrefois, Aatrox et ses frères étaient honorés pour avoir défendu Shurima contre le Néant. Mais ils finirent par devenir une menace plus grande encore pour Runeterra : la ruse et la sorcellerie furent employées pour les battre. Cependant, après des...", nomenclature.getDescription());
            assertEquals("Épée des Darkin", nomenclature.getTitle());
            assertEquals(20, nomenclature.getStats().size());
            assertEquals(650, nomenclature.getStats().get("hp"));
            assertEquals(List.of("Fighter"), nomenclature.getTags());
        }
    }

    @Test
    void testUpdateAllNomenclature_ITEM_RUNE_QUEUE() throws IOException, URISyntaxException {
        AtomicInteger urlConstructorCount = new AtomicInteger();
        try (MockedConstruction<URI> uriMock = mockConstruction(URI.class, (urlConstructor, context) -> {
            int urlConstructorCountInt = urlConstructorCount.getAndIncrement();
            String nomenclature = urlConstructorCountInt == 0 ? "version" : urlConstructorCountInt == 1 ? "item" : urlConstructorCountInt== 2 ? "rune" : "queue";
            when(urlConstructor.toURL()).thenReturn(getJson(nomenclature));
        })) {

            LOLVersionEntity version = new LOLVersionEntity("14.19.1");
            version.setItem("14.18.1");
            when(versionRepository.findFirstByOrderByItemAsc()).thenReturn(version);
            ArgumentCaptor<List<LOLItemNomenclature>> itemArgumentCaptor = ArgumentCaptor.forClass(List.class);
            ArgumentCaptor<List<LOLRuneNomenclature>> runeArgumentCaptor = ArgumentCaptor.forClass(List.class);
            ArgumentCaptor<List<LOLQueueNomenclature>> queueArgumentCaptor = ArgumentCaptor.forClass(List.class);

            assertTrue(nomenclatureService.updateAllNomenclatures());

            assertEquals(4, uriMock.constructed().size());
            verify(versionRepository, times(1)).findFirstByOrderByItemAsc();
            verify(versionRepository, times(1)).save(any());
            verifyItem(itemArgumentCaptor);
            verifyRune(runeArgumentCaptor);
            verifyQueue(queueArgumentCaptor);
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

    private void verifyRune(ArgumentCaptor<List<LOLRuneNomenclature>> argumentCaptor) {
        verify(runeNomenclatureRepository, times(1)).saveAll(argumentCaptor.capture());
        assertFalse(argumentCaptor.getValue().isEmpty());
        LOLRuneNomenclature categoryNomenclature = argumentCaptor.getValue().get(0);
        LOLRuneNomenclature perkNomenclature = argumentCaptor.getValue().get(1);
        assertEquals("8200", categoryNomenclature.getId());
        assertEquals("8214", perkNomenclature.getId());
        assertEquals("perk-images/Styles/7202_Sorcery.png", categoryNomenclature.getImage());
        assertEquals("perk-images/Styles/Sorcery/SummonAery/SummonAery.png", perkNomenclature.getImage());
        assertEquals("Sorcellerie", categoryNomenclature.getName());
        assertEquals("Invocation d'Aery", perkNomenclature.getName());
        assertEquals("Vos attaques et vos compétences envoient Aery vers une cible, blessant les ennemis ou protégeant les alliés.", perkNomenclature.getDescription());
        assertEquals("Blesser un champion ennemi avec une attaque de base ou une compétence envoie Aery vers lui, lui infligeant 10 - 50 selon le niveau (+<scaleAP>0.05 puissance</scaleAP>) (+<scaleAD>0.1 dégâts d'attaque supplémentaires</scaleAD>).<br><br>Renforcer ou protéger un allié avec une compétence envoie Aery vers lui, lui octroyant un bouclier absorbant 30 - 100 selon le niveau (+<scaleAP>0.05 puissance</scaleAP>) (+<scaleAD>0.1 dégâts d'attaque supplémentaires</scaleAD>).<br><br>Aery ne peut plus être envoyée tant qu'elle n'est pas revenue auprès de vous.", perkNomenclature.getLongDescription());
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

    @Test
    void testUpdateAllNomenclature_AUGMENT() throws IOException, URISyntaxException {
        AtomicInteger urlConstructorCount = new AtomicInteger();
        try (MockedConstruction<URI> uriMock = mockConstruction(URI.class, (urlConstructor, context) ->
                when(urlConstructor.toURL()).thenReturn(getJson(urlConstructorCount.getAndIncrement() == 0 ? "version" : "augment")))) {

            LOLVersionEntity version = new LOLVersionEntity("14.19.1");
            version.setAugment("14.18.1");
            when(versionRepository.findFirstByOrderByItemAsc()).thenReturn(version);
            ArgumentCaptor<List<LOLAugmentNomenclature>> argumentCaptor = ArgumentCaptor.forClass(List.class);

            assertTrue(nomenclatureService.updateAllNomenclatures());

            assertEquals(2, uriMock.constructed().size());
            verify(versionRepository, times(1)).findFirstByOrderByItemAsc();
            verify(versionRepository, times(1)).save(any());
            verify(augmentNomenclatureRepository, times(1)).saveAll(argumentCaptor.capture());
            assertFalse(argumentCaptor.getValue().isEmpty());
            LOLAugmentNomenclature nomenclature = argumentCaptor.getValue().get(0);
            System.out.println(nomenclature.getImage());
            assertEquals("108", nomenclature.getId());
            assertEquals("Autodestruction", nomenclature.getName());
            assertEquals("assets/ux/cherry/augments/icons/selfdestruct_large.png", nomenclature.getImage());
            assertEquals("Vous commencez chaque manche avec une bombe attachée à vous. Au bout de @BombDelay@ sec, elle explose en infligeant des <trueDamage>dégâts bruts équivalents à @MaxHealthDamage*100@% des PV max</trueDamage> et <status>projette dans les airs</status> pendant @KnockupDuration@ sec.<br>", nomenclature.getDescription());
            assertFalse(nomenclature.getDataValues().isEmpty());
            assertEquals(15, nomenclature.getDataValues().get("BombDelay"));
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
