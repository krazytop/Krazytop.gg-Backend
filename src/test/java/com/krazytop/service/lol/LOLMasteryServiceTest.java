package com.krazytop.service.lol;

import com.krazytop.config.ApplicationContextProvider;
import com.krazytop.config.SpringConfiguration;
import com.krazytop.entity.lol.LOLMasteryEntity;
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

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LOLMasteryServiceTest {

    @InjectMocks
    private LOLMasteryService masteryService;

    @Mock
    private LOLMasteryRepository masteryRepository;
    @Mock
    private RIOTApiKeyRepository apiKeyRepository;
    @Mock
    private LOLChampionNomenclatureRepository championNomenclatureRepository;

    @Test
    void testGetLocalMasteries() {
        when(masteryRepository.findAllByPuuid(anyString())).thenReturn(List.of(new LOLMasteryEntity()));
        assertFalse(masteryService.getLocalMasteries("puuid").isEmpty());
        verify(masteryRepository, times(1)).findAllByPuuid(anyString());
    }

    private void mockRepositories(MockedStatic<SpringConfiguration> springConfigurationMock) {
        ApplicationContextProvider mockApplicationContextProvider = mock(ApplicationContextProvider.class);
        ApplicationContext mockApplicationContext = mock(ApplicationContext.class);

        springConfigurationMock.when(SpringConfiguration::contextProvider).thenReturn(mockApplicationContextProvider);
        when(mockApplicationContextProvider.getApplicationContext()).thenReturn(mockApplicationContext);

        when(mockApplicationContext.getBean(LOLChampionNomenclatureRepository.class)).thenReturn(championNomenclatureRepository);

        when(championNomenclatureRepository.findFirstById(anyString())).thenReturn(new LOLChampionNomenclature());
        when(apiKeyRepository.findFirstByOrderByKeyAsc()).thenReturn(new RIOTApiKeyEntity("API_KEY"));
    }

    @Test
    void testUpdateRemoteToLocalMasteries_OK() throws IOException, URISyntaxException {
        URL masteriesUrl = new File(String.format("%s/src/test/resources/lol/masteries.json", System.getProperty("user.dir"))).toURI().toURL();
        try (MockedConstruction<URI> uriMock = mockConstruction(URI.class, (urlConstructor, context) ->
                when(urlConstructor.toURL()).thenReturn(masteriesUrl))) {

            try (MockedStatic<SpringConfiguration> springConfigurationMock = mockStatic(SpringConfiguration.class)) {
                this.mockRepositories(springConfigurationMock);

                masteryService.updateRemoteToLocalMasteries("puuid");

                assertEquals(1, uriMock.constructed().size());
                verify(championNomenclatureRepository, times(1)).findFirstById(anyString());
                verify(apiKeyRepository, times(1)).findFirstByOrderByKeyAsc();
            }
        }
    }

    @Test
    void testUpdateRemoteToLocalMasteries_IOException() {
        try (MockedConstruction<URI> uriMock = mockConstruction(URI.class, (urlConstructor, context) ->
                when(urlConstructor.toURL()).thenThrow(MalformedURLException.class))) {

            when(apiKeyRepository.findFirstByOrderByKeyAsc()).thenReturn(new RIOTApiKeyEntity("API_KEY"));

            assertThrows(MalformedURLException.class, () -> masteryService.updateRemoteToLocalMasteries("puuid"));

            assertEquals(1, uriMock.constructed().size());
            verify(championNomenclatureRepository, times(0)).findFirstById(anyString());
            verify(apiKeyRepository, times(1)).findFirstByOrderByKeyAsc();
        }
    }
}