package com.krazytop.service.riot;

import com.krazytop.config.CustomHTTPException;
import com.krazytop.entity.riot.RIOTBoardEntity;
import com.krazytop.entity.riot.RIOTSummonerEntity;
import com.krazytop.http_responses.RIOTHTTPErrorResponsesEnum;
import com.krazytop.nomenclature.GameEnum;
import com.krazytop.repository.lol.LOLBoardRepository;
import com.krazytop.repository.riot.RIOTBoardRepository;
import com.krazytop.repository.riot.RIOTRankRepository;
import com.krazytop.repository.tft.TFTBoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class RIOTBoardService {

    private final LOLBoardRepository lolBoardRepository;
    private final TFTBoardRepository tftBoardRepository;
    private final RIOTSummonerService summonerService;

    @Autowired
    public RIOTBoardService(LOLBoardRepository lolBoardRepository, TFTBoardRepository tftBoardRepository, RIOTSummonerService summonerService) {
        this.lolBoardRepository = lolBoardRepository;
        this.tftBoardRepository = tftBoardRepository;
        this.summonerService = summonerService;
    }

    public Optional<RIOTBoardEntity> getBoard(String boardId, GameEnum game) {
        return getRepository(game).findById(boardId);
    }

    public String createBoard(GameEnum game) {
        RIOTBoardEntity board = new RIOTBoardEntity();
        getRepository(game).save(board);
        return board.getId();
    }

    public RIOTSummonerEntity addSummonerToBoard(String boardId, String region, String tag, String name, GameEnum game) {
        Optional<RIOTBoardEntity> board = getBoard(boardId, game);
        if (board.isPresent()) {
            RIOTSummonerEntity summoner = summonerService.getSummoner(region, tag, name, GameEnum.LOL);
            if (!board.get().getSummonerIds().contains(summoner.getId())) {
                board.get().getSummonerIds().add(summoner.getId());
                getRepository(game).save(board.get());
            } else {
                throw new CustomHTTPException(RIOTHTTPErrorResponsesEnum.SUMMONER_ALREADY_ADDED_TO_BOARD);
            }
            return summoner;
        } else {
            throw new CustomHTTPException(RIOTHTTPErrorResponsesEnum.BOARD_NOT_FOUND);
        }
    }

    public void removeSummonerOfBoard(String boardId, String summonerId, GameEnum game) {
        Optional<RIOTBoardEntity> board = getBoard(boardId, game);
        if (board.isPresent()) {
            if (board.get().getSummonerIds().contains(summonerId)) {
                board.get().getSummonerIds().remove(summonerId);
                getRepository(game).save(board.get());
            } else {
                throw new CustomHTTPException(RIOTHTTPErrorResponsesEnum.SUMMONER_ABSENT_OF_BOARD);
            }
        } else {
            throw new CustomHTTPException(RIOTHTTPErrorResponsesEnum.BOARD_NOT_FOUND);
        }
    }

    private RIOTBoardRepository getRepository(GameEnum game) {
        return game == GameEnum.LOL ? lolBoardRepository : tftBoardRepository;
    }

}
