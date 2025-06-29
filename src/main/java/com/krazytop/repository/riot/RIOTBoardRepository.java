package com.krazytop.repository.riot;

import com.krazytop.entity.riot.RIOTBoard;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RIOTBoardRepository extends MongoRepository<RIOTBoard, String> {
}
