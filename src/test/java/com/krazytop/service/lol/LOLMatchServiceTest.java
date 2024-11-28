package com.krazytop.service.lol;

import com.krazytop.config.ApplicationContextProvider;
import com.krazytop.config.SpringConfiguration;
import com.krazytop.entity.api_key.ApiKeyEntity;
import com.krazytop.entity.lol.LOLMatchEntity;
import com.krazytop.nomenclature.GameEnum;
import com.krazytop.nomenclature.lol.*;
import com.krazytop.repository.api_key.ApiKeyRepository;
import com.krazytop.repository.lol.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
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
    private LOLAugmentNomenclatureRepository augmentNomenclatureRepository;
    @Mock
    private LOLQueueNomenclatureRepository queueNomenclatureRepository;
    @Mock
    private ApiKeyRepository apiKeyRepository;

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

    private void mockRepositories(MockedStatic<SpringConfiguration> springConfigurationMock, String queueId, boolean mockAugmentRepository) {
        ApplicationContextProvider mockApplicationContextProvider = mock(ApplicationContextProvider.class);
        ApplicationContext mockApplicationContext = mock(ApplicationContext.class);

        springConfigurationMock.when(SpringConfiguration::contextProvider).thenReturn(mockApplicationContextProvider);
        when(mockApplicationContextProvider.getApplicationContext()).thenReturn(mockApplicationContext);

        when(mockApplicationContext.getBean(LOLChampionNomenclatureRepository.class)).thenReturn(championNomenclatureRepository);
        when(mockApplicationContext.getBean(LOLItemNomenclatureRepository.class)).thenReturn(itemNomenclatureRepository);
        when(mockApplicationContext.getBean(LOLRuneNomenclatureRepository.class)).thenReturn(runeNomenclatureRepository);
        when(mockApplicationContext.getBean(LOLSummonerSpellNomenclatureRepository.class)).thenReturn(summonerSpellNomenclatureRepository);
        when(mockApplicationContext.getBean(LOLQueueNomenclatureRepository.class)).thenReturn(queueNomenclatureRepository);
        if (mockAugmentRepository) when(mockApplicationContext.getBean(LOLAugmentNomenclatureRepository.class)).thenReturn(augmentNomenclatureRepository);

        when(championNomenclatureRepository.findFirstById(anyString())).thenReturn(new LOLChampionNomenclature());
        when(itemNomenclatureRepository.findFirstById(anyString())).thenReturn(new LOLItemNomenclature());
        when(runeNomenclatureRepository.findFirstById(anyString())).thenReturn(new LOLRuneNomenclature());
        if (mockAugmentRepository) when(augmentNomenclatureRepository.findFirstById(anyString())).thenReturn(new LOLAugmentNomenclature());
        when(summonerSpellNomenclatureRepository.findFirstById(anyString())).thenReturn(new LOLSummonerSpellNomenclature());
        LOLQueueNomenclature compatibleQueue = new LOLQueueNomenclature();
        compatibleQueue.setId(queueId);
        when(queueNomenclatureRepository.findFirstById(anyString())).thenReturn(compatibleQueue);
        when(apiKeyRepository.findFirstByGame(GameEnum.RIOT)).thenReturn(new ApiKeyEntity(GameEnum.RIOT, "API_KEY"));
        when(matchRepository.findFirstById(anyString())).thenReturn(null);
    }

    @Test
    void testUpdateRemoteToLocalMatches_NormalGame() {
        AtomicInteger urlConstructorCount = new AtomicInteger();
        URL emptyMatchIdsUrl = getJson("empty-match-ids");
        URL matchIdsUrl = getJson("match-ids");
        URL matchUrl = getJson("match");
        try (MockedConstruction<URI> uriMock = mockConstruction(URI.class, (urlConstructor, context) -> {
            int urlConstructorCountInt = urlConstructorCount.getAndIncrement();
            when(urlConstructor.toURL()).thenReturn(urlConstructorCountInt == 0 ? matchIdsUrl : urlConstructorCountInt == 1 ? matchUrl : emptyMatchIdsUrl);
        })) {

            try (MockedStatic<SpringConfiguration> springConfigurationMock = mockStatic(SpringConfiguration.class)) {
                this.mockRepositories(springConfigurationMock, "450", false);
                ArgumentCaptor<LOLMatchEntity> argumentCaptor = ArgumentCaptor.forClass(LOLMatchEntity.class);

                assertDoesNotThrow(() -> matchService.updateRemoteToLocalMatches("puuid", 0, false));

                assertEquals(3, uriMock.constructed().size());
                verify(matchRepository, times(1)).findFirstById(anyString());
                verify(apiKeyRepository, times(3)).findFirstByGame(GameEnum.RIOT);
                verify(matchRepository, times(1)).save(argumentCaptor.capture());
                assertNotNull(argumentCaptor.getValue());
                LOLMatchEntity match = argumentCaptor.getValue();
                assertEquals(2, match.getTeams().size());
                match.getTeams().forEach(team -> assertEquals(5, team.getParticipants().size()));
            }
        }
    }

    @Test
    void testUpdateRemoteToLocalMatches_Arena() {
        AtomicInteger urlConstructorCount = new AtomicInteger();
        URL emptyMatchIdsUrl = getJson("empty-match-ids");
        URL matchIdsUrl = getJson("match-ids");
        URL matchUrl = getJson("match-arena");
        try (MockedConstruction<URI> uriMock = mockConstruction(URI.class, (urlConstructor, context) -> {
            int urlConstructorCountInt = urlConstructorCount.getAndIncrement();
            when(urlConstructor.toURL()).thenReturn(urlConstructorCountInt == 0 ? matchIdsUrl : urlConstructorCountInt == 1 ? matchUrl : emptyMatchIdsUrl);
        })) {

            try (MockedStatic<SpringConfiguration> springConfigurationMock = mockStatic(SpringConfiguration.class)) {
                this.mockRepositories(springConfigurationMock, "1710", true);
                ArgumentCaptor<LOLMatchEntity> argumentCaptor = ArgumentCaptor.forClass(LOLMatchEntity.class);

                assertDoesNotThrow(() -> matchService.updateRemoteToLocalMatches("puuid", 0, false));

                assertEquals(3, uriMock.constructed().size());
                verify(matchRepository, times(1)).findFirstById(anyString());
                verify(apiKeyRepository, times(3)).findFirstByGame(GameEnum.RIOT);
                verify(matchRepository, times(1)).save(argumentCaptor.capture());
                assertNotNull(argumentCaptor.getValue());
                LOLMatchEntity match = argumentCaptor.getValue();
                assertEquals(8, match.getTeams().size());
                match.getTeams().forEach(team -> assertEquals(2, team.getParticipants().size()));
            }
        }
    }

    @Test
    void testUpdateRemoteToLocalMatches_IncompatibleQueue() {
        AtomicInteger urlConstructorCount = new AtomicInteger();
        URL emptyMatchIdsUrl = getJson("empty-match-ids");
        URL matchIdsUrl = getJson("match-ids");
        URL matchUrl = getJson("match");
        try (MockedConstruction<URI> uriMock = mockConstruction(URI.class, (urlConstructor, context) -> {
            int urlConstructorCountInt = urlConstructorCount.getAndIncrement();
            when(urlConstructor.toURL()).thenReturn(urlConstructorCountInt == 0 ? matchIdsUrl : urlConstructorCountInt == 1 ? matchUrl : emptyMatchIdsUrl);
        })) {
            try (MockedStatic<SpringConfiguration> springConfigurationMock = mockStatic(SpringConfiguration.class)) {
                this.mockRepositories(springConfigurationMock, "999", false);

                assertDoesNotThrow(() -> matchService.updateRemoteToLocalMatches("puuid", 0, false));

                assertEquals(3, uriMock.constructed().size());
                verify(matchRepository, times(1)).findFirstById(anyString());
                verify(apiKeyRepository, times(3)).findFirstByGame(GameEnum.RIOT);
                verify(matchRepository, times(0)).save(any());
            }
        }
    }

    @Test
    void testUpdateRemoteToLocalMatches_ExistingMatch() {
        URL matchIdsUrl = getJson("match-ids");
        try (MockedConstruction<URI> uriMock = mockConstruction(URI.class, (urlConstructor, context) ->
                when(urlConstructor.toURL()).thenReturn(matchIdsUrl))) {

            when(apiKeyRepository.findFirstByGame(GameEnum.RIOT)).thenReturn(new ApiKeyEntity(GameEnum.RIOT, "API_KEY"));
            when(matchRepository.findFirstById(anyString())).thenReturn(new LOLMatchEntity());

            assertDoesNotThrow(() -> matchService.updateRemoteToLocalMatches("puuid", 0, false));

            assertEquals(2, uriMock.constructed().size());
            verify(matchRepository, times(2)).findFirstById(anyString());
            verify(apiKeyRepository, times(2)).findFirstByGame(GameEnum.RIOT);
        }
    }

    @Test
    void testUpdateRemoteToLocalMatches_IOException() {
        try (MockedConstruction<URI> uriMock = mockConstruction(URI.class, (urlConstructor, context) ->
                when(urlConstructor.toURL()).thenThrow(MalformedURLException.class))) {

            when(apiKeyRepository.findFirstByGame(GameEnum.RIOT)).thenReturn(new ApiKeyEntity(GameEnum.RIOT, "API_KEY"));

            assertThrows(IOException.class, () -> matchService.updateRemoteToLocalMatches("puuid", 0, false));

            assertEquals(1, uriMock.constructed().size());
            verify(matchRepository, times(0)).findFirstById(anyString());
            verify(apiKeyRepository, times(1)).findFirstByGame(GameEnum.RIOT);
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