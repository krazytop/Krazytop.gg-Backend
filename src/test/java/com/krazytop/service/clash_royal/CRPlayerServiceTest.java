package com.krazytop.service.clash_royal;

import com.krazytop.config.ApplicationContextProvider;
import com.krazytop.config.SpringConfiguration;
import com.krazytop.entity.api_key.ApiKeyEntity;
import com.krazytop.entity.clash_royal.CRBadgeEntity;
import com.krazytop.entity.clash_royal.CRCardEntity;
import com.krazytop.entity.clash_royal.CRClanEntity;
import com.krazytop.entity.clash_royal.CRPlayerEntity;
import com.krazytop.nomenclature.GameEnum;
import com.krazytop.nomenclature.clash_royal.CRAccountLevelNomenclature;
import com.krazytop.nomenclature.clash_royal.CRArenaNomenclature;
import com.krazytop.nomenclature.clash_royal.CRCardNomenclature;
import com.krazytop.nomenclature.clash_royal.CRCardRarityNomenclature;
import com.krazytop.repository.api_key.ApiKeyRepository;
import com.krazytop.repository.clash_royal.*;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationContext;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CRPlayerServiceTest {

    @InjectMocks
    private CRPlayerService playerService;

    @Mock
    private CRPlayerRepository playerRepository;
    @Mock
    private ApiKeyRepository apiKeyRepository;
    @Mock
    private CRArenaNomenclatureRepository arenaNomenclatureRepository;
    @Mock
    private CRAccountLevelNomenclatureRepository accountLevelNomenclatureRepository;
    @Mock
    private CRCardNomenclatureRepository cardNomenclatureRepository;
    @Mock
    private CRCardRarityNomenclatureRepository cardRarityNomenclatureRepository;

    @Test
    void testGetLocalPlayer() {
        when(playerRepository.findFirstById(anyString())).thenReturn(new CRPlayerEntity());
        assertNotNull(playerService.getLocalPlayer("puuid"));
        verify(playerRepository, times(1)).findFirstById(anyString());
    }

    private void mockRepositories(MockedStatic<SpringConfiguration> springConfigurationMock) {
        ApplicationContextProvider mockApplicationContextProvider = mock(ApplicationContextProvider.class);
        ApplicationContext mockApplicationContext = mock(ApplicationContext.class);

        springConfigurationMock.when(SpringConfiguration::contextProvider).thenReturn(mockApplicationContextProvider);
        when(mockApplicationContextProvider.getApplicationContext()).thenReturn(mockApplicationContext);

        when(mockApplicationContext.getBean(CRArenaNomenclatureRepository.class)).thenReturn(arenaNomenclatureRepository);
        when(mockApplicationContext.getBean(CRCardNomenclatureRepository.class)).thenReturn(cardNomenclatureRepository);
        when(mockApplicationContext.getBean(CRAccountLevelNomenclatureRepository.class)).thenReturn(accountLevelNomenclatureRepository);
        when(mockApplicationContext.getBean(CRCardRarityNomenclatureRepository.class)).thenReturn(cardRarityNomenclatureRepository);

        when(arenaNomenclatureRepository.findFirstById(anyInt())).thenReturn(new CRArenaNomenclature());
        when(cardNomenclatureRepository.findFirstById(anyInt())).thenReturn(new CRCardNomenclature());
        CRCardRarityNomenclature cardRarityNomenclature = new CRCardRarityNomenclature();
        cardRarityNomenclature.setUpgradeCost(List.of(2, 4, 10, 20, 50, 100, 200, 400, 800, 1000, 1500, 3000, 5000, 50000));
        when(cardRarityNomenclatureRepository.findFirstByName(anyString())).thenReturn(cardRarityNomenclature);
        CRAccountLevelNomenclature accountLevelNomenclature = new CRAccountLevelNomenclature();
        accountLevelNomenclature.setSummonerLevel(51);
        when(accountLevelNomenclatureRepository.findFirstByLevel(anyInt())).thenReturn(new CRAccountLevelNomenclature());
        when(apiKeyRepository.findFirstByGame(GameEnum.CLASH_ROYAL)).thenReturn(new ApiKeyEntity(GameEnum.RIOT, "API_KEY"));
    }

    @Test
    void testUpdateRemoteToLocalPlayer_OK() throws IOException {
        FileInputStream playerInputStream = new FileInputStream(String.format("%s/src/test/resources/clash-royal/player.json", System.getProperty("user.dir")));
        try (MockedStatic<SpringConfiguration> springConfigurationMock = mockStatic(SpringConfiguration.class)) {
            try (MockedStatic<HttpClients> httpClientsMock = mockStatic(HttpClients.class)) {
                try (CloseableHttpClient closeableHttpClient = mock(CloseableHttpClient.class)) {
                    httpClientsMock.when(HttpClients::createDefault).thenReturn(closeableHttpClient);
                    CloseableHttpResponse response = mock(CloseableHttpResponse.class);
                    when(closeableHttpClient.execute(any())).thenReturn(response);
                    HttpEntity httpEntity = mock(HttpEntity.class);
                    when(response.getEntity()).thenReturn(httpEntity);
                    when(httpEntity.getContent()).thenReturn(playerInputStream);

                    this.mockRepositories(springConfigurationMock);
                    ArgumentCaptor<CRPlayerEntity> playerArgumentCaptor = ArgumentCaptor.forClass(CRPlayerEntity.class);

                    assertDoesNotThrow(() -> playerService.updateRemoteToLocalPlayer("29UR0L2J"));

                    verify(arenaNomenclatureRepository, times(1)).findFirstById(anyInt());
                    verify(cardNomenclatureRepository, times(3)).findFirstById(anyInt());
                    verify(cardRarityNomenclatureRepository, times(3)).findFirstByName(anyString());
                    verify(accountLevelNomenclatureRepository, times(1)).findFirstByLevel(anyInt());
                    verify(apiKeyRepository, times(1)).findFirstByGame(GameEnum.CLASH_ROYAL);
                    verify(playerRepository, times(1)).save(playerArgumentCaptor.capture());

                    verifyPlayer(playerArgumentCaptor);
                }
            }
        }
    }

    private void verifyPlayer(ArgumentCaptor<CRPlayerEntity> playerArgumentCaptor) {
        CRPlayerEntity player = playerArgumentCaptor.getValue();
        assertNotNull(player);
        assertEquals("29UR0L2J", player.getId());
        assertEquals("Krazytop", player.getName());
        assertEquals("Krazytop", player.getBestTrophies());
        assertEquals("Krazytop", player.getChallengeCardsWon());
        assertEquals("Krazytop", player.getThreeCrownWins());
        assertEquals("Krazytop", player.getExpPoints());
        assertEquals("Krazytop", player.getLosses());
        assertEquals("Krazytop", player.getWins());
        assertEquals("Krazytop", player.getClanWarCardsWon());
        assertEquals("Krazytop", player.getStarPoints());
        assertEquals("Krazytop", player.getTotalDonations());
        assertNotNull(player.getUpdateDate());
        verifyClan(player.getClan());
        verifyAccountLevelNomenclature(player.getAccountLevelNomenclature());
        verifyCard(player.getCurrentFavouriteCard());
        verifyArenaNomenclature(player.getArenaNomenclature());
        List<CRBadgeEntity> badges = player.getBadges();
        assertFalse(badges.isEmpty());
        verifyBadge(badges.get(0));
        List<CRCardEntity> cards = player.getCards();
        assertFalse(cards.isEmpty());
        verifyCard(cards.get(0));
    }

    private void verifyBadge(CRBadgeEntity badge) {
        assertEquals("Krazytop", badge.getImage());
        assertEquals("Krazytop", badge.getName());
        assertEquals("Krazytop", badge.getLevel());
        assertEquals("Krazytop", badge.getTarget());
        assertEquals("Krazytop", badge.getProgress());
        assertEquals("Krazytop", badge.getMaxLevel());
    }

    private void verifyAccountLevelNomenclature(CRAccountLevelNomenclature accountLevelNomenclature) {
        assertNotNull(accountLevelNomenclature);
        assertEquals("Krazytop", accountLevelNomenclature.getLevel());
        assertEquals("Krazytop", accountLevelNomenclature.getExpToNextLevel());
        assertEquals("Krazytop", accountLevelNomenclature.getTowerLevel());
        assertEquals("Krazytop", accountLevelNomenclature.getSummonerLevel());
    }

    private void verifyClan(CRClanEntity clan) {
        assertNotNull(clan);
        assertEquals("Krazytop", clan.getId());
        assertEquals("Krazytop", clan.getName());
        assertEquals("Krazytop", clan.getBadge());
    }

    private void verifyArenaNomenclature(CRArenaNomenclature arenaNomenclature) {
        assertNotNull(arenaNomenclature);
        assertEquals("Krazytop", arenaNomenclature.getImage());
        assertEquals("Krazytop", arenaNomenclature.getName());
        assertEquals("Krazytop", arenaNomenclature.getId());
    }

    private void verifyCard(CRCardEntity card) {
        assertNotNull(card);
        assertEquals(1, card.getLevel());
        assertEquals(1, card.getCount());
        assertEquals(1, card.getUpgradeCost());
        assertEquals(1, card.getEvolutionLevel());
        assertEquals(1, card.getStarLevel());
        CRCardNomenclature nomenclature = card.getNomenclature();
        assertNotNull(nomenclature);
        assertEquals(1, nomenclature.getRarity());
        assertEquals(1, nomenclature.getImage());
        assertEquals(1, nomenclature.getType());
        assertEquals(1, nomenclature.getDescription());
        assertEquals(1, nomenclature.getName());
        assertEquals(1, nomenclature.getId());
        assertEquals(1, nomenclature.getElixir());
    }

/*
    @Test
    void testUpdateRemoteToLocalPlayer_IOException() {
        try (MockedConstruction<URI> uriMock = mockConstruction(URI.class, (urlConstructor, context) ->
                when(urlConstructor.toURL()).thenThrow(MalformedURLException.class))) {

            when(apiKeyRepository.findFirstByGame(GameEnum.RIOT)).thenReturn(new ApiKeyEntity(GameEnum.RIOT, "API_KEY"));

            assertThrows(MalformedURLException.class, () -> masteryService.updateRemoteToLocalMasteries("puuid"));

            assertEquals(1, uriMock.constructed().size());
            verify(championNomenclatureRepository, times(0)).findFirstById(anyString());
            verify(apiKeyRepository, times(1)).findFirstByGame(GameEnum.RIOT);
        }
    }*/
}