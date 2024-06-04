package com.krazytop.api.clash_royal;

import com.krazytop.entity.clash_royal.CRApiKeyEntity;
import com.krazytop.repository.clash_royal.CRApiKeyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CRApiKeyApi {

    private final CRApiKeyRepository crApiKeyRepository;

    @Autowired
    public CRApiKeyApi(CRApiKeyRepository crApiKeyRepository) {
        this.crApiKeyRepository = crApiKeyRepository;
    }

    public CRApiKeyEntity getApiKey() {
        return crApiKeyRepository.findFirstByOrderByKeyAsc();
    }
}
