package com.krazytop.repository.tft;

import com.krazytop.entity.tft.TFTVersionEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TFTVersionRepository extends MongoRepository<TFTVersionEntity, String> {

    TFTVersionEntity findFirstByOrderByVersionAsc();

}
