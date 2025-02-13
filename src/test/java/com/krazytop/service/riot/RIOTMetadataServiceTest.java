package com.krazytop.service.riot;

import com.krazytop.entity.riot.RIOTMetadataEntity;
import com.krazytop.repository.riot.RIOTMetadataRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RIOTMetadataServiceTest {

    @InjectMocks
    private RIOTMetadataService metadataService;

    @Mock
    private RIOTMetadataRepository metadataRepository;

    @Test
    void testGetMetadata() {
        when(metadataRepository.findFirstByOrderByIdAsc()).thenReturn(Optional.of(new RIOTMetadataEntity()));

        assertNotNull(metadataService.getMetadata());

        verify(metadataRepository, times(1)).findFirstByOrderByIdAsc();
    }

    @Test
    void testUpdateMetadata() {
        when(metadataRepository.findFirstByOrderByIdAsc()).thenReturn(Optional.of(new RIOTMetadataEntity()));
        ArgumentCaptor<RIOTMetadataEntity> metadataArgumentCaptor = ArgumentCaptor.forClass(RIOTMetadataEntity.class);

        metadataService.updateMetadata(metadata -> metadata.setCurrentLOLSeason(14));

        verify(metadataRepository, times(1)).findFirstByOrderByIdAsc();
        verify(metadataRepository, times(1)).save(metadataArgumentCaptor.capture());
        assertEquals(14, metadataArgumentCaptor.getValue().getCurrentLOLSeason());
    }

}