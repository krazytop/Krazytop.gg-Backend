package com.krazytop.service.lol;

import com.krazytop.config.ApplicationContextProvider;
import com.krazytop.config.SpringConfiguration;
import com.krazytop.entity.lol.LOLMatchEntity;
import com.krazytop.entity.riot.RIOTApiKeyEntity;
import com.krazytop.nomenclature.lol.*;
import com.krazytop.repository.lol.*;
import com.krazytop.repository.riot.RIOTApiKeyRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedConstruction;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LOLMatchServiceTest {

    @InjectMocks
    private LOLMatchService matchService;

    @Mock
    private LOLMatchRepository matchRepository;
    @Mock
    private LOLChampionNomenclatureRepository championNomenclatureRepository;
    @Mock
    private LOLItemNomenclatureRepository itemNomenclatureRepository;
    @Mock
    private LOLRuneNomenclatureRepository runeNomenclatureRepository;
    @Mock
    private LOLSummonerSpellNomenclatureRepository summonerSpellNomenclatureRepository;
    @Mock
    private LOLQueueNomenclatureRepository queueNomenclatureRepository;
    @Mock
    private RIOTApiKeyRepository apiKeyRepository;

    @Test
    void testGetMatchesCount_AllQueues_AllRoles() {
        when(matchRepository.countAll(anyString())).thenReturn(1L);
        assertEquals(1L, matchService.getLocalMatchesCount("puuid", "all-queues", "all-roles"));
        verify(matchRepository, times(1)).countAll(anyString());
    }

    @Test
    void testGetMatchesCount_AllQueues_SpecificRole() {
        when(matchRepository.countAllByRole(anyString(), anyString())).thenReturn(2L);
        assertEquals(2L, matchService.getLocalMatchesCount("puuid", "all-queues", "jungle"));
        verify(matchRepository, times(1)).countAllByRole(anyString(), anyString());
    }

    @Test
    void testGetMatchesCount_SpecificQueue_AllRoles() {
        when(matchRepository.countAllByQueue(anyString(), anyList())).thenReturn(3L);
        assertEquals(3L, matchService.getLocalMatchesCount("puuid", "aram", "all-roles"));
        verify(matchRepository, times(1)).countAllByQueue(anyString(), anyList());
    }

    @Test
    void testGetMatchesCount_SpecificQueue_SpecificRole() {
        when(matchRepository.countAllByQueueAndByRole(anyString(), anyList(), anyString())).thenReturn(4L);
        assertEquals(4L, matchService.getLocalMatchesCount("puuid", "aram", "jungle"));
        verify(matchRepository, times(1)).countAllByQueueAndByRole(anyString(), anyList(), anyString());
    }

    @Test
    void testGetMatches_AllQueues_AllRoles() {
        ReflectionTestUtils.setField(matchService, "pageSize", 1);
        Page<LOLMatchEntity> page = new PageImpl<>(List.of(new LOLMatchEntity()));
        when(matchRepository.findAll(anyString(), any())).thenReturn(page);
        assertFalse(matchService.getLocalMatches("puuid", 0, "all-queues", "all-roles").isEmpty());
        verify(matchRepository, times(1)).findAll(anyString(), any());
    }

    @Test
    void testGetMatches_AllQueues_SpecificRole() {
        ReflectionTestUtils.setField(matchService, "pageSize", 1);
        Page<LOLMatchEntity> page = new PageImpl<>(List.of(new LOLMatchEntity()));
        when(matchRepository.findAllByRole(anyString(), anyString(), any())).thenReturn(page);
        assertFalse(matchService.getLocalMatches("puuid", 0, "all-queues", "jungle").isEmpty());
        verify(matchRepository, times(1)).findAllByRole(anyString(), anyString(), any());
    }

    @Test
    void testGetMatches_SpecificQueue_AllRoles() {
        ReflectionTestUtils.setField(matchService, "pageSize", 1);
        Page<LOLMatchEntity> page = new PageImpl<>(List.of(new LOLMatchEntity()));
        when(matchRepository.findAllByQueue(anyString(), anyList(), any())).thenReturn(page);
        assertFalse(matchService.getLocalMatches("puuid", 0, "aram", "all-roles").isEmpty());
        verify(matchRepository, times(1)).findAllByQueue(anyString(), anyList(), any());
    }

    @Test
    void testGetMatches_SpecificQueue_SpecificRole() {
        ReflectionTestUtils.setField(matchService, "pageSize", 1);
        Page<LOLMatchEntity> page = new PageImpl<>(List.of(new LOLMatchEntity()));
        when(matchRepository.findAllByQueueAndByRole(anyString(), anyList(), anyString(), any())).thenReturn(page);
        assertFalse(matchService.getLocalMatches("puuid", 0, "aram", "jungle").isEmpty());
        verify(matchRepository, times(1)).findAllByQueueAndByRole(anyString(), anyList(), anyString(), any());
    }

    private void mockRepositories(MockedStatic<SpringConfiguration> springConfigurationMock, String queueId) {
        ApplicationContextProvider mockApplicationContextProvider = mock(ApplicationContextProvider.class);
        ApplicationContext mockApplicationContext = mock(ApplicationContext.class);

        springConfigurationMock.when(SpringConfiguration::contextProvider).thenReturn(mockApplicationContextProvider);
        when(mockApplicationContextProvider.getApplicationContext()).thenReturn(mockApplicationContext);

        when(mockApplicationContext.getBean(LOLChampionNomenclatureRepository.class)).thenReturn(championNomenclatureRepository);
        when(mockApplicationContext.getBean(LOLItemNomenclatureRepository.class)).thenReturn(itemNomenclatureRepository);
        when(mockApplicationContext.getBean(LOLRuneNomenclatureRepository.class)).thenReturn(runeNomenclatureRepository);
        when(mockApplicationContext.getBean(LOLSummonerSpellNomenclatureRepository.class)).thenReturn(summonerSpellNomenclatureRepository);
        when(mockApplicationContext.getBean(LOLQueueNomenclatureRepository.class)).thenReturn(queueNomenclatureRepository);

        when(championNomenclatureRepository.findFirstById(anyString())).thenReturn(new LOLChampionNomenclature());
        when(itemNomenclatureRepository.findFirstById(anyString())).thenReturn(new LOLItemNomenclature());
        when(runeNomenclatureRepository.findFirstById(anyString())).thenReturn(new LOLRuneNomenclature());
        when(summonerSpellNomenclatureRepository.findFirstById(anyString())).thenReturn(new LOLSummonerSpellNomenclature());
        LOLQueueNomenclature compatibleQueue = new LOLQueueNomenclature();
        compatibleQueue.setId(queueId);
        when(queueNomenclatureRepository.findFirstById(anyString())).thenReturn(compatibleQueue);
        when(apiKeyRepository.findFirstByOrderByKeyAsc()).thenReturn(new RIOTApiKeyEntity("API_KEY"));
        when(matchRepository.findFirstById(anyString())).thenReturn(null);
    }

    @Test
    void testUpdateRemoteToLocalMatches_NewMatch_CompatibleQueue() {
        AtomicInteger urlConstructorCount = new AtomicInteger();
        URL matchIdsUrl = getJson("match-ids");
        URL matchUrl = getJson("match");
        try (MockedConstruction<URI> uriMock = mockConstruction(URI.class, (urlConstructor, context) ->
                when(urlConstructor.toURL()).thenReturn(urlConstructorCount.getAndIncrement() == 0 ? matchIdsUrl : matchUrl))) {

            try (MockedStatic<SpringConfiguration> springConfigurationMock = mockStatic(SpringConfiguration.class)) {
                this.mockRepositories(springConfigurationMock, "450");

                assertDoesNotThrow(() -> matchService.updateRemoteToLocalMatches("puuid"));

                assertEquals(2, uriMock.constructed().size());
                verify(matchRepository, times(1)).findFirstById(anyString());
                verify(apiKeyRepository, times(2)).findFirstByOrderByKeyAsc();
                verify(matchRepository, times(1)).save(any());
            }
        }
    }

    @Test
    void testUpdateRemoteToLocalMatches_NewMatch_IncompatibleQueue() {
        AtomicInteger urlConstructorCount = new AtomicInteger();
        URL matchIdsUrl = getJson("match-ids");
        URL matchUrl = getJson("match");
        try (MockedConstruction<URI> uriMock = mockConstruction(URI.class, (urlConstructor, context) ->
                when(urlConstructor.toURL()).thenReturn(urlConstructorCount.getAndIncrement() == 0 ? matchIdsUrl : matchUrl))) {

            try (MockedStatic<SpringConfiguration> springConfigurationMock = mockStatic(SpringConfiguration.class)) {
                this.mockRepositories(springConfigurationMock, "999");

                assertDoesNotThrow(() -> matchService.updateRemoteToLocalMatches("puuid"));

                assertEquals(2, uriMock.constructed().size());
                verify(matchRepository, times(1)).findFirstById(anyString());
                verify(apiKeyRepository, times(2)).findFirstByOrderByKeyAsc();
                verify(matchRepository, times(0)).save(any());
            }
        }
    }

    @Test
    void testUpdateRemoteToLocalMatches_ExistingMatch() {
        URL matchIdsUrl = getJson("match-ids");
        try (MockedConstruction<URI> uriMock = mockConstruction(URI.class, (urlConstructor, context) ->
                when(urlConstructor.toURL()).thenReturn(matchIdsUrl))) {

            when(apiKeyRepository.findFirstByOrderByKeyAsc()).thenReturn(new RIOTApiKeyEntity("API_KEY"));
            when(matchRepository.findFirstById(anyString())).thenReturn(new LOLMatchEntity());

            assertDoesNotThrow(() -> matchService.updateRemoteToLocalMatches("puuid"));

            assertEquals(1, uriMock.constructed().size());
            verify(matchRepository, times(1)).findFirstById(anyString());
            verify(apiKeyRepository, times(1)).findFirstByOrderByKeyAsc();
        }
    }

    @Test
    void testUpdateRemoteToLocalMatches_IOException() {
        try (MockedConstruction<URI> uriMock = mockConstruction(URI.class, (urlConstructor, context) ->
                when(urlConstructor.toURL()).thenThrow(MalformedURLException.class))) {

            when(apiKeyRepository.findFirstByOrderByKeyAsc()).thenReturn(new RIOTApiKeyEntity("API_KEY"));

            assertThrows(IOException.class, () -> matchService.updateRemoteToLocalMatches("puuid"));

            assertEquals(1, uriMock.constructed().size());
            verify(matchRepository, times(0)).findFirstById(anyString());
            verify(apiKeyRepository, times(1)).findFirstByOrderByKeyAsc();
        }
    }

    private URL getJson(String fileName) {
        try {
            return new File(String.format("%s/src/test/resources/lol/%s.json", System.getProperty("user.dir"), fileName)).toURI().toURL();
        } catch (MalformedURLException e) {
            return null;
        }
    }
}