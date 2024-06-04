package com.krazytop.api.tft;

import com.krazytop.entity.tft.TFTMatchEntity;
import com.krazytop.repository.tft.TFTMatchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TFTMatchApi {

    private final TFTMatchRepository tftMatchRepository;
    @Value("${spring.data.web.pageable.default-page-size:5}")
    private int pageSize;

    @Autowired
    public TFTMatchApi(TFTMatchRepository tftMatchRepository) {
        this.tftMatchRepository = tftMatchRepository;
    }

    public List<TFTMatchEntity> getMatches(String puuid, int pageNb, String queue, String set) {
        PageRequest pageRequest = PageRequest.of(pageNb, pageSize);
        if (queue == null) {
            return tftMatchRepository.findByParticipantsPuuidAndSetOrderByDatetimeDesc(puuid, set, pageRequest).getContent();
        } else {
            return tftMatchRepository.findByParticipantsPuuidAndQueueQueueTypeAndSetOrderByDatetimeDesc(puuid, queue, set, pageRequest).getContent();
        }
    }

    public void updateMatch(TFTMatchEntity match) {
        tftMatchRepository.save(match);
    }

    public long getMatchCount(String puuid, String queue, String set) {
        if (queue == null) {
            return tftMatchRepository.countByParticipantsPuuidAndSet(puuid, set);
        } else {
            return tftMatchRepository.countByParticipantsPuuidAndQueueQueueTypeAndSet(puuid, queue,set);
        }
    }

    public TFTMatchEntity getMatch(String matchId) {
        return tftMatchRepository.findFirstById(matchId);
    }


}
