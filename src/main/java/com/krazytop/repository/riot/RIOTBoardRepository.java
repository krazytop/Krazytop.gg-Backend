package com.krazytop.repository.riot;

import com.krazytop.entity.riot.RIOTBoardEntity;
import com.krazytop.entity.riot.rank.RIOTRankEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface RIOTBoardRepository extends MongoRepository<RIOTBoardEntity, String> {
}
