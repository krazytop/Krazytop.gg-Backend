package com.krazytop.controller.lol;

import com.krazytop.service.lol.LOLStatsService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LOLStatsControllerTest {

    @InjectMocks
    private LOLStatsController statsController;
    @Mock
    private LOLStatsService statsService;

    @Test
    void getLatestMatchesResult() {
        when(statsService.getLatestMatchesResult(anyString(), anyString(), anyString())).thenReturn(List.of("VICTORY"));
        assertEquals(1, Objects.requireNonNull(statsController.getLatestMatchesResult("puuid", "queue", "role").getBody()).size());
        verify(statsService, times(1)).getLatestMatchesResult(anyString(), anyString(), anyString());
    }
}