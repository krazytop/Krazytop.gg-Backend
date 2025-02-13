package com.krazytop.service.riot;

import com.krazytop.entity.api_key.ApiKeyEntity;
import com.krazytop.entity.riot.rank.RIOTRankEntity;
import com.krazytop.entity.riot.RIOTSummonerEntity;
import com.krazytop.nomenclature.GameEnum;
import com.krazytop.repository.api_key.ApiKeyRepository;
import com.krazytop.repository.riot.RIOTRankRepository;
import com.krazytop.repository.riot.RIOTSummonerRepository;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RIOTRankServiceTest {

    @InjectMocks
    private RIOTRankService rankService;

    @Mock
    private ApiKeyRepository apiKeyRepository;
    @Mock
    private RIOTSummonerRepository summonerRepository;
    @Mock
    private RIOTRankRepository rankRepository;

    @Test
    void testUpdateRemoteToLocalRank_FIRST_RANK() {
        try (MockedConstruction<URI> uriMock = mockConstruction(URI.class, (urlConstructor, context) ->
            when(urlConstructor.toURL()).thenReturn(getJson("rank")))) {
            when(summonerRepository.findFirstByPuuid(anyString())).thenReturn(new RIOTSummonerEntity());
            when(apiKeyRepository.findFirstByGame(GameEnum.RIOT)).thenReturn(new ApiKeyEntity(GameEnum.RIOT, "key"));
            when(rankRepository.findFirstByPuuid(anyString())).thenReturn(null);
            ArgumentCaptor<RIOTRankEntity> rankArgumentCaptor = ArgumentCaptor.forClass(RIOTRankEntity.class);

            assertDoesNotThrow(() -> rankService.updateRemoteToLocalRank("puuid", "url", 13, rankRepository));

            assertEquals(1, uriMock.constructed().size());
            verify(summonerRepository, times(1)).findFirstByPuuid(anyString());
            verify(apiKeyRepository, times(1)).findFirstByGame(GameEnum.RIOT);
            verify(rankRepository, times(1)).save(rankArgumentCaptor.capture());
            RIOTRankEntity rank = rankArgumentCaptor.getValue();
            assertNotNull(rank);
            assertEquals("puuid", rank.getPuuid());
            RIOTRankEntity.RankInformations turbo = rank.getRanks().get(13).get("RANKED_TFT_TURBO").get(0);
            RIOTRankEntity.RankInformations ranked = rank.getRanks().get(13).get("RANKED_TFT").get(0);
            assertEquals("ORANGE", turbo.getTier());
            assertNull(turbo.getRank());
            assertEquals(7194, turbo.getLeaguePoints());
            assertNotNull(turbo.getDate());
            assertEquals(208, turbo.getWins());
            assertEquals(87, turbo.getLosses());
            assertEquals("CHALLENGER", ranked.getTier());
            assertEquals("I", ranked.getRank());
            assertEquals(1118, ranked.getLeaguePoints());
            assertNotNull(ranked.getDate());
            assertEquals(150, ranked.getWins());
            assertEquals(82, ranked.getLosses());
        }
    }

    @Test
    void testUpdateRemoteToLocalRank_NO_SUMMONER() {
        when(summonerRepository.findFirstByPuuid(anyString())).thenReturn(null);
        ArgumentCaptor<RIOTRankEntity> rankArgumentCaptor = ArgumentCaptor.forClass(RIOTRankEntity.class);

        assertDoesNotThrow(() -> rankService.updateRemoteToLocalRank("puuid", "url", 13, rankRepository));

        verify(summonerRepository, times(1)).findFirstByPuuid(anyString());
        verify(rankRepository, times(0)).save(rankArgumentCaptor.capture());
    }

    private URL getJson(String fileName) {
        try {
            return new File(String.format("%s/src/test/resources/riot/%s.json", System.getProperty("user.dir"), fileName)).toURI().toURL();
        } catch (MalformedURLException e) {
            return null;
        }
    }

}