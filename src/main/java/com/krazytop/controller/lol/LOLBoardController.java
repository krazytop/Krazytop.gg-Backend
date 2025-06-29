package com.krazytop.controller.lol;

import com.krazytop.api_gateway.api.generated.LeagueOfLegendsBoardApi;
import com.krazytop.api_gateway.model.generated.RIOTBoardDTO;
import com.krazytop.api_gateway.model.generated.RIOTSummonerDTO;
import com.krazytop.service.riot.RIOTBoardService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class LOLBoardController implements LeagueOfLegendsBoardApi {

    private static final Logger LOGGER = LoggerFactory.getLogger(LOLBoardController.class);

    private final RIOTBoardService boardService;

    @Autowired
    public LOLBoardController(RIOTBoardService boardService) {
        this.boardService = boardService;
    }

    @Override
    public ResponseEntity<RIOTBoardDTO> getBoard(@PathVariable String boardId) {
        LOGGER.info("Retrieving LOL board");
        RIOTBoardDTO board = boardService.getBoardDTO(boardId);
        LOGGER.info("LOL board retrieved");
        return new ResponseEntity<>(board, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> updateBoard(@PathVariable String boardId) {
        LOGGER.info("Updating LOL board");
        boardService.updateBoard(boardId);
        LOGGER.info("LOL board updated");
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<String> createBoard() {
        LOGGER.info("Creating LOL board");
        String boardId = boardService.createBoard();
        LOGGER.info("LOL board created");
        return new ResponseEntity<>(boardId, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<RIOTSummonerDTO> addSummonerToBoard(@PathVariable String boardId, @PathVariable String tag, @PathVariable String name) {
        LOGGER.info("Updating LOL board with the new summoner");
        RIOTSummonerDTO newSummoner = boardService.addSummonerToBoard(boardId, tag, name);
        LOGGER.info("LOL board updated with the new summoner");
        return new ResponseEntity<>(newSummoner, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> removeSummonerOfBoard(@PathVariable String boardId, @PathVariable String puuid) {
        LOGGER.info("Removing summoner of the LOL board");
        boardService.removeSummonerOfBoard(boardId, puuid);
        LOGGER.info("Summoner removed of the LOL board");
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> updateBoardName(@PathVariable String boardId, @PathVariable String name) {
        LOGGER.info("Updating LOL board name");
        boardService.updateBoardName(boardId, name);
        LOGGER.info("LOL board name updated");
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> deleteBoard(@PathVariable String boardId) {
        LOGGER.info("Deleting LOL board");
        boardService.deleteBoard(boardId);
        LOGGER.info("LOL board deleted");
        return new ResponseEntity<>(HttpStatus.OK);
    }
}