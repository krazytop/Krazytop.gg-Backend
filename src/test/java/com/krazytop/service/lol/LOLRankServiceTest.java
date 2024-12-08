package com.krazytop.service.lol;

import com.krazytop.entity.lol.LOLVersionEntity;
import com.krazytop.entity.riot.RIOTRankEntity;
import com.krazytop.repository.lol.LOLRankRepository;
import com.krazytop.repository.lol.LOLVersionRepository;
import com.krazytop.repository.riot.RIOTRankRepository;
import com.krazytop.service.riot.RIOTRankService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.net.URISyntaxException;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LOLRankServiceTest {

    @InjectMocks
    private LOLRankService rankService;

    @Mock
    private LOLRankRepository rankRepository;
    @Mock
    private LOLVersionRepository versionRepository;
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
        when(versionRepository.findFirstByOrderByItemAsc()).thenReturn(new LOLVersionEntity("1"));

        assertDoesNotThrow(() -> rankService.updateRemoteToLocalRank("puuid"));

        verify(versionRepository, times(1)).findFirstByOrderByItemAsc();
        verify(riotRankService, times(1)).updateRemoteToLocalRank(anyString(), anyString(), anyInt(), any(RIOTRankRepository.class));
    }

}