package com.krazytop.api.clash_royal;

import com.krazytop.entity.clash_royal.CRPlayerEntity;
import com.krazytop.repository.clash_royal.CRPlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Component
public class CRPlayerApi {

    private final CRPlayerRepository crPlayerRepository;

    @Autowired
    public CRPlayerApi(CRPlayerRepository crPlayerRepository) {
        this.crPlayerRepository = crPlayerRepository;
    }

    public CRPlayerEntity getPlayer(String playerId) {
        return crPlayerRepository.findFirstById(playerId);
    }

    public CRPlayerEntity updatePlayer(CRPlayerEntity player) {
        player.setUpdateDate(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()));
        return crPlayerRepository.save(player);
    }

}
