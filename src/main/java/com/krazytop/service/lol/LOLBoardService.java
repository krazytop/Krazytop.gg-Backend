package com.krazytop.service.lol;

import com.krazytop.api_gateway.model.generated.RIOTBoardDTO;
import com.krazytop.api_gateway.model.generated.RIOTSummonerDTO;
import com.krazytop.entity.riot.RIOTBoard;
import com.krazytop.entity.riot.RIOTSummoner;
import com.krazytop.exception.CustomException;
import com.krazytop.exception.ApiErrorEnum;
import com.krazytop.mapper.lol.LOLBoardMapper;
import com.krazytop.repository.lol.LOLBoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Service
public class LOLBoardService {

    private final LOLBoardRepository boardRepository;
    private final LOLSummonerService summonerService;
    private final LOLMatchService matchService;
    private final LOLMasteryService masteryService;
    private final LOLRankService rankService;
    private final LOLBoardMapper boardMapper;

    @Autowired
    public LOLBoardService(LOLBoardRepository boardRepository, LOLSummonerService summonerService, LOLMatchService matchService, LOLMasteryService masteryService, LOLRankService rankService, LOLBoardMapper boardMapper) {
        this.boardRepository = boardRepository;
        this.summonerService = summonerService;
        this.matchService = matchService;
        this.masteryService = masteryService;
        this.rankService = rankService;
        this.boardMapper = boardMapper;
    }

    public RIOTBoardDTO getBoardDTO(String boardId) {
        return boardMapper.toDTO(getBoard(boardId));
    }

    public RIOTBoard getBoard(String boardId) {
        return boardRepository.findById(boardId).orElseThrow(() -> new CustomException(ApiErrorEnum.BOARD_NOT_FOUND));
    }

    public String createBoard() {
        RIOTBoard board = new RIOTBoard();
        boardRepository.save(board);
        return board.getId();
    }

    public RIOTSummonerDTO addSummonerToBoard(String boardId, String tag, String name) {
        RIOTBoard board = getBoard(boardId);
        RIOTSummonerDTO summoner = summonerService.getSummonerDTO(tag, name);
        if (!board.getPuuids().contains(summoner.getPuuid())) {
            board.getPuuids().add(summoner.getPuuid());
            boardRepository.save(board);
        } else {
            throw new CustomException(ApiErrorEnum.SUMMONER_ALREADY_ADDED_TO_BOARD);
        }
        return summoner;
    }

    public void removeSummonerOfBoard(String boardId, String puuid) {
        RIOTBoard board = getBoard(boardId);
        if (board.getPuuids().contains(puuid)) {
            board.getPuuids().remove(puuid);
            boardRepository.save(board);
        } else {
            throw new CustomException(ApiErrorEnum.SUMMONER_ABSENT_OF_BOARD);
        }
    }

    public void updateBoard(String boardId) {
        RIOTBoard board = getBoard(boardId);
        board.getPuuids().forEach(puuid -> {
            RIOTSummoner summoner = summonerService.getSummoner(puuid);
            if (summoner.getUpdateDate() != null) {
                summonerService.updateSummoner(summoner.getPuuid());
                rankService.updateRanks(summoner.getPuuid());
                matchService.updateMatches(summoner.getPuuid());
                masteryService.updateMasteries(summoner.getPuuid());
            }
        });
        board.setUpdateDate(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()));
        boardRepository.save(board);
    }

    public void updateBoardName(String boardId, String name) {
        RIOTBoard board = getBoard(boardId);
        board.setName(name);
        boardRepository.save(board);
    }

    public void deleteBoard(String boardId) {
        getBoard(boardId);
        boardRepository.deleteById(boardId);
    }

}
