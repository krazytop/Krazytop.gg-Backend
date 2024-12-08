package com.krazytop.service.tft;

import com.krazytop.entity.riot.RIOTRankEntity;
import com.krazytop.entity.tft.TFTVersionEntity;
import com.krazytop.repository.riot.RIOTRankRepository;
import com.krazytop.repository.tft.TFTRankRepository;
import com.krazytop.repository.tft.TFTVersionRepository;
import com.krazytop.service.riot.RIOTRankService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.net.URISyntaxException;

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
    private TFTVersionRepository versionRepository;
    @Mock
    private RIOTRankService riotRankService;

    @Test
    void testGetLocalRank() {
        when(rankRepository.findFirstByPuuid(anyString())).thenReturn(new RIOTRankEntity());

        assertNotNull(rankService.getLocalRank("puuid"));

        verify(rankRepository, times(1)).findFirstByPuuid(anyString());
    }

    @Test
    void testUpdateRemoteToLocalRank() throws URISyntaxException, IOException {
        when(versionRepository.findFirstByOrderByOfficialVersionAsc()).thenReturn(new TFTVersionEntity("1", "2", 3));

        assertDoesNotThrow(() -> rankService.updateRemoteToLocalRank("puuid"));

        verify(versionRepository, times(1)).findFirstByOrderByOfficialVersionAsc();
        verify(riotRankService, times(1)).updateRemoteToLocalRank(anyString(), anyString(), anyInt(), any(RIOTRankRepository.class));
    }

}