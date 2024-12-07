package com.krazytop.service.tft;

import com.krazytop.entity.tft.TFTVersionEntity;
import com.krazytop.nomenclature.tft.TFTQueueNomenclature;
import com.krazytop.repository.tft.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedConstruction;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TFTNomenclatureServiceTest {

    @InjectMocks
    private TFTNomenclatureService nomenclatureService;

    @Mock
    private TFTVersionRepository versionRepository;
    @Mock
    private TFTTraitNomenclatureRepository traitNomenclatureRepository;
    @Mock
    private TFTUnitNomenclatureRepository unitNomenclatureRepository;
    @Mock
    private TFTQueueNomenclatureRepository queueNomenclatureRepository;
    @Mock
    private TFTItemNomenclatureRepository itemNomenclatureRepository;

    @Test
    void testUpdateAllNomenclature_OFFICIAL() throws IOException, URISyntaxException {
        AtomicInteger urlConstructorCount = new AtomicInteger();
        try (MockedConstruction<URI> uriMock = mockConstruction(URI.class, (urlConstructor, context) -> {
                    int constructorCount = urlConstructorCount.getAndIncrement();
                    when(urlConstructor.toURL()).thenReturn(getJson(constructorCount == 0 ? "official-dragon-version" : constructorCount == 1 ? "community-dragon-version" : "official-dragon"));
                })) {

            TFTVersionEntity version = new TFTVersionEntity("14.23.6369832+branch.releases-14-23.content.release", "14.18.1", 12);
            when(versionRepository.findFirstByOrderByOfficialVersionAsc()).thenReturn(version);
            ArgumentCaptor<TFTVersionEntity> versionArgumentCaptor = ArgumentCaptor.forClass(TFTVersionEntity.class);
            ArgumentCaptor<List<TFTQueueNomenclature>> queuesArgumentCaptor = ArgumentCaptor.forClass(List.class);

            assertTrue(nomenclatureService.updateAllNomenclatures());

            assertEquals(2, uriMock.constructed().size());
            verify(versionRepository, times(1)).findFirstByOrderByOfficialVersionAsc();
            verify(versionRepository, times(1)).save(versionArgumentCaptor.capture());
            assertEquals("14.19.1", versionArgumentCaptor.getValue().getOfficialVersion());
            verify(queueNomenclatureRepository, times(1)).saveAll(queuesArgumentCaptor.capture());
            assertFalse(queuesArgumentCaptor.getValue().isEmpty());
            TFTQueueNomenclature nomenclature = queuesArgumentCaptor.getValue().get(0);
            assertEquals("1130", nomenclature.getId());
            assertEquals("TFTM_ModeIcon_Turbo.png", nomenclature.getImage());
            assertEquals("HYPER ROLL", nomenclature.getName());
            assertEquals("RANKED_TFT_TURBO", nomenclature.getQueueType());
        }
    }

    private URL getJson(String nomenclature) {
        try {
            return new File(String.format("%s/src/test/resources/tft/nomenclature/%s.json", System.getProperty("user.dir"), nomenclature)).toURI().toURL();
        } catch (MalformedURLException e) {
            return null;
        }
    }

}
