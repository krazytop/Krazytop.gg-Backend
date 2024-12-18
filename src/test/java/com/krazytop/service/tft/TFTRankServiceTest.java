package com.krazytop.service.tft;

import com.krazytop.entity.riot.RIOTMetadataEntity;
import com.krazytop.entity.riot.RIOTRankEntity;
import com.krazytop.repository.riot.RIOTRankRepository;
import com.krazytop.repository.tft.TFTRankRepository;
import com.krazytop.service.riot.RIOTMetadataService;
import com.krazytop.service.riot.RIOTRankService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TFTRankServiceTest {

    @InjectMocks
    private TFTRankService rankService;

    @Mock
    private TFTRankRepository rankRepository;
    @Mock
    private RIOTRankService riotRankService;
    @Mock
    private RIOTMetadataService metadataService;

    @Test
    void testGetLocalRank() {
        when(rankRepository.findFirstByPuuid(anyString())).thenReturn(new RIOTRankEntity());

        assertNotNull(rankService.getLocalRank("puuid"));

        verify(rankRepository, times(1)).findFirstByPuuid(anyString());
    }

    @Test
    void testUpdateRemoteToLocalRank() throws URISyntaxException, IOException {
        when(metadataService.getMetadata()).thenReturn(new RIOTMetadataEntity("1", 1, 2, List.of()));

        assertDoesNotThrow(() -> rankService.updateRemoteToLocalRank("puuid"));

        verify(metadataService, times(1)).getMetadata();
        verify(riotRankService, times(1)).updateRemoteToLocalRank(anyString(), anyString(), anyInt(), any(RIOTRankRepository.class));
    }

}