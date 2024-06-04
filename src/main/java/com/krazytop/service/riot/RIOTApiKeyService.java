package com.krazytop.service.riot;

import com.krazytop.api.riot.RIOTApiKeyApi;
import com.krazytop.entity.riot.RIOTApiKeyEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RIOTApiKeyService {

    private final RIOTApiKeyApi riotApiKeyApi;

    @Autowired
    public RIOTApiKeyService(RIOTApiKeyApi riotApiKeyApi) {
        this.riotApiKeyApi = riotApiKeyApi;
    }

    public RIOTApiKeyEntity getApiKey() {
        return riotApiKeyApi.getApiKey();
    }

}
