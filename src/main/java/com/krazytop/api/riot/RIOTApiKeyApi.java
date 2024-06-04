package com.krazytop.api.riot;

import com.krazytop.entity.riot.RIOTApiKeyEntity;
import com.krazytop.repository.riot.RIOTApiKeyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RIOTApiKeyApi {

    private final RIOTApiKeyRepository riotApiKeyRepository;

    @Autowired
    public RIOTApiKeyApi(RIOTApiKeyRepository riotApiKeyRepository) {
        this.riotApiKeyRepository = riotApiKeyRepository;
    }

    public RIOTApiKeyEntity getApiKey() {
        return riotApiKeyRepository.findFirstByOrderByKeyAsc();
    }
}
