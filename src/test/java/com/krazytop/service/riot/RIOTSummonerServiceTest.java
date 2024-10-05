package com.krazytop.service.riot;

import com.krazytop.entity.api_key.ApiKeyEntity;
import com.krazytop.entity.riot.RIOTSummonerEntity;
import com.krazytop.nomenclature.GameEnum;
import com.krazytop.repository.api_key.ApiKeyRepository;
import com.krazytop.repository.riot.RIOTSummonerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RIOTSummonerServiceTest {

    @InjectMocks
    private RIOTSummonerService summonerService;

    @Mock
    private ApiKeyRepository apiKeyRepository;
    @Mock
    private RIOTSummonerRepository summonerRepository;

    @Test
    void testGetLocalSummoner() {
        when(summonerRepository.findFirstByRegionAndTagAndName(anyString(), anyString(), anyString())).thenReturn(new RIOTSummonerEntity());
        assertNotNull(summonerService.getLocalSummoner("puuid", "tag", "name"));
        verify(summonerRepository, times(1)).findFirstByRegionAndTagAndName(anyString(), anyString(), anyString());
    }

    @Test
    void testUpdateRemoteToLocalSummoner() {
        AtomicInteger urlConstructorCount = new AtomicInteger();
        try (MockedConstruction<URI> uriMock = mockConstruction(URI.class, (urlConstructor, context) ->
            when(urlConstructor.toURL()).thenReturn(getJson(urlConstructorCount.getAndIncrement() == 0 ? "account" : "summoner")))) {

            when(apiKeyRepository.findFirstByGame(GameEnum.RIOT)).thenReturn(new ApiKeyEntity(GameEnum.RIOT, "API_KEY"));
            ArgumentCaptor<RIOTSummonerEntity> summonerArgumentCaptor = ArgumentCaptor.forClass(RIOTSummonerEntity.class);

            assertDoesNotThrow(() -> summonerService.updateRemoteToLocalSummoner("region", "tag", "name"));

            assertEquals(2, uriMock.constructed().size());
            verify(apiKeyRepository, times(1)).findFirstByGame(GameEnum.RIOT);
            verify(summonerRepository, times(1)).save(summonerArgumentCaptor.capture());
            RIOTSummonerEntity summoner = summonerArgumentCaptor.getValue();
            assertNotNull(summoner);
            assertEquals("Ab3cZzd93ORw04O9T0TGFNMtgFfNp8Wda9An8-RX63dLBzDC", summoner.getId());
            assertEquals("aUHcgK9wmHkRaDKWN0qqzsuyLusbWWheFWMizdzFHGYP9lakq-oi3x_xnxsHtYVqzyZ1ywVJ8Jg21Q", summoner.getPuuid());
            assertEquals(1641, summoner.getIcon());
            assertEquals("EUW", summoner.getTag());
            assertEquals("krazytop", summoner.getName());
            assertEquals(464, summoner.getLevel());
            assertEquals("region", summoner.getRegion());
            assertEquals("Ag76GWohO8QCBrYs8ora19KfcPfWYvnXFm2zL8lCEAAPH080ecSzMY7r", summoner.getAccountId());
            assertNotNull(summoner.getUpdateDate());
        }
    }

    private URL getJson(String fileName) {
        try {
            return new File(String.format("%s/src/test/resources/riot/%s.json", System.getProperty("user.dir"), fileName)).toURI().toURL();
        } catch (MalformedURLException e) {
            return null;
        }
    }
}