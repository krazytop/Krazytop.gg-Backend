package com.krazytop.repository.lol;

import com.krazytop.entity.riot.RIOTBoard;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface LOLBoardRepository extends MongoRepository<RIOTBoard, String> {
}
