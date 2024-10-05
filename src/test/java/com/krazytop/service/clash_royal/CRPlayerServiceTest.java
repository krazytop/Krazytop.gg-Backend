package com.krazytop.service.clash_royal;

import com.krazytop.config.ApplicationContextProvider;
import com.krazytop.config.SpringConfiguration;
import com.krazytop.entity.api_key.ApiKeyEntity;
import com.krazytop.entity.clash_royal.*;
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
        FileInputStream upcomingChestsInputStream = new FileInputStream(String.format("%s/src/test/resources/clash-royal/upcoming-chests.json", System.getProperty("user.dir")));
        try (MockedStatic<SpringConfiguration> springConfigurationMock = mockStatic(SpringConfiguration.class);
             MockedStatic<HttpClients> httpClientsMock = mockStatic(HttpClients.class);
             CloseableHttpClient closeableHttpClient = mock(CloseableHttpClient.class)) {
            httpClientsMock.when(HttpClients::createDefault).thenReturn(closeableHttpClient);
            CloseableHttpResponse response = mock(CloseableHttpResponse.class);
            when(closeableHttpClient.execute(any())).thenReturn(response);
            HttpEntity httpEntity = mock(HttpEntity.class);
            when(response.getEntity()).thenReturn(httpEntity);
            when(httpEntity.getContent()).thenReturn(playerInputStream).thenReturn(upcomingChestsInputStream);

            this.mockRepositories(springConfigurationMock);
            ArgumentCaptor<CRPlayerEntity> playerArgumentCaptor = ArgumentCaptor.forClass(CRPlayerEntity.class);

            assertDoesNotThrow(() -> playerService.updateRemoteToLocalPlayer("29UR0L2J"));

            verify(arenaNomenclatureRepository, times(1)).findFirstById(anyInt());
            verify(cardNomenclatureRepository, times(3)).findFirstById(anyInt());
            verify(cardRarityNomenclatureRepository, times(3)).findFirstByName(anyString());
            verify(accountLevelNomenclatureRepository, times(1)).findFirstByLevel(anyInt());
            verify(apiKeyRepository, times(2)).findFirstByGame(GameEnum.CLASH_ROYAL);
            verify(playerRepository, times(1)).save(playerArgumentCaptor.capture());

            verifyPlayer(playerArgumentCaptor);
        }
    }

    private void verifyPlayer(ArgumentCaptor<CRPlayerEntity> playerArgumentCaptor) {
        CRPlayerEntity player = playerArgumentCaptor.getValue();
        assertNotNull(player);
        assertEquals("29UR0L2J", player.getId());
        assertEquals("Krazytop", player.getName());
        assertEquals(7764, player.getBestTrophies());
        assertEquals(8671, player.getChallengeCardsWon());
        assertEquals(1934, player.getThreeCrownWins());
        assertEquals(12730, player.getExpPoints());
        assertEquals(3428, player.getLosses());
        assertEquals(4702, player.getWins());
        assertEquals(52054, player.getClanWarCardsWon());
        assertEquals(25750, player.getStarPoints());
        assertEquals(85991, player.getTotalDonations());
        assertNotNull(player.getUpdateDate());
        verifyClan(player.getClan());
        assertNotNull(player.getAccountLevelNomenclature());
        verifyCard(player.getCurrentFavouriteCard());
        assertNotNull(player.getArenaNomenclature());
        List<CRBadgeEntity> badges = player.getBadges();
        assertFalse(badges.isEmpty());
        verifyBadge(badges.get(0));
        verifyCards(player.getCards());
        verifyCards(player.getCurrentDeck());
        verifyTrophies(player.getSeasonsTrophies());
        verifyLeagues(player.getSeasonsLeagues());
        verifyUpcomingChests(player.getUpcomingChests());
    }

    private void verifyTrophies(CRTrophiesEntity trophies) {
        verifyTrophy(trophies.getPreviousSeason(), "2024-08", 5059);
        verifyTrophy(trophies.getCurrentSeason(), null, 7737);
        verifyTrophy(trophies.getBestSeason(), "2021-12", 6022);
    }

    private void verifyTrophy(CRTrophyEntity trophy, String expectedDate, int expectedTrophies) {
        assertNotNull(trophy);
        assertEquals(expectedDate, trophy.getDate());
        assertEquals(expectedTrophies, trophy.getTrophies());
    }

    private void verifyLeagues(CRLeaguesEntity leagues) {
        verifyLeague(leagues.getPreviousSeason(), 4, 5);
        verifyLeague(leagues.getCurrentSeason(), 3, 2);
        verifyLeague(leagues.getBestSeason(), 0, 2);
    }

    private void verifyLeague(CRLeagueEntity league, int expectedTrophies, int expectedLeagueNumber) {
        assertNotNull(league);
        assertEquals(0, league.getRank());
        assertEquals(expectedLeagueNumber, league.getLeagueNumber());
        assertEquals(expectedTrophies, league.getTrophies());
    }

    private void verifyBadge(CRBadgeEntity badge) {
        assertEquals("https://api-assets.clashroyale.com/playerbadges/512/T9iTL2NzRUCMK5KoV3p9to5vxhUUvsooT-DMxwIvSCg.png", badge.getImage());
        assertEquals("YearsPlayed", badge.getName());
        assertEquals(8, badge.getLevel());
        assertEquals(3285, badge.getTarget());
        assertEquals(3144, badge.getProgress());
        assertEquals(9, badge.getMaxLevel());
    }

    private void verifyClan(CRClanEntity clan) {
        assertNotNull(clan);
        assertEquals("#9CYPG92U", clan.getId());
        assertEquals("Gard National", clan.getName());
        assertEquals(16000078, clan.getBadge());
    }

    private void verifyCards(List<CRCardEntity> cards) {
        assertFalse(cards.isEmpty());
        verifyCard(cards.get(0));
    }

    private void verifyCard(CRCardEntity card) {
        assertNotNull(card);
        assertEquals(13, card.getLevel());
        assertEquals(3010, card.getCount());
        assertEquals(5000, card.getUpgradeCost());
        assertEquals(1, card.getEvolutionLevel());
        assertEquals(2, card.getStarLevel());
        assertNotNull(card.getNomenclature());
    }

    private void verifyUpcomingChests(List<CRChestEntity> chests) {
        assertFalse(chests.isEmpty());
        CRChestEntity chest = chests.get(0);
        assertEquals("Golden Chest", chest.getName());
        assertEquals(9, chest.getIndex());
    }
}