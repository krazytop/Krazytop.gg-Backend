package com.krazytop.controller.lol;

import com.krazytop.entity.lol.LOLRankEntity;
import com.krazytop.service.lol.LOLRankService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LOLRankControllerTest {

    @InjectMocks
    private LOLRankController rankController;
    @Mock
    private LOLRankService rankService;

    @Test
    void getLocalRank() {
        when(rankService.getLocalRank(anyString(), anyString())).thenReturn(new LOLRankEntity());
        ResponseEntity<LOLRankEntity> response = rankController.getLocalRank("puuid", "queue");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(rankService, times(1)).getLocalRank(anyString(), anyString());
    }

    @Test
    void updateRemoteToLocalRank() {
        when(rankService.updateRemoteToLocalRank(anyString())).thenReturn(List.of(new LOLRankEntity()));
        ResponseEntity<List<LOLRankEntity>> response = rankController.updateRemoteToLocalRank("puuid");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, Objects.requireNonNull(response.getBody()).size());
        verify(rankService, times(1)).updateRemoteToLocalRank(anyString());
    }
}