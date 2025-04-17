package com.krazytop.controller.riot;

import com.krazytop.config.CustomHTTPException;
import com.krazytop.entity.riot.RIOTBoardEntity;
import com.krazytop.entity.riot.RIOTSummonerEntity;
import com.krazytop.http_responses.RIOTHTTPErrorResponsesEnum;
import com.krazytop.nomenclature.GameEnum;
import com.krazytop.service.riot.RIOTBoardService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class RIOTBoardController {

    private static final Logger LOGGER = LoggerFactory.getLogger(RIOTBoardController.class);

    private final RIOTBoardService boardService;

    @Autowired
    public RIOTBoardController(RIOTBoardService boardService) {
        this.boardService = boardService;
    }

    @GetMapping("/lol/board/{boardId}")
    public ResponseEntity<RIOTBoardEntity> getLOLBoard(@PathVariable String boardId) {
        return getBoard(boardId, GameEnum.LOL);
    }

    @GetMapping("/tft/board/{boardId}")
    public ResponseEntity<RIOTBoardEntity> geTFTBoard(@PathVariable String boardId) {
        return getBoard(boardId, GameEnum.TFT);
    }

    private ResponseEntity<RIOTBoardEntity> getBoard(String boardId, GameEnum game) {
        LOGGER.info("Retrieving {} board", game);
        return new ResponseEntity<>(boardService.getBoard(boardId, game)
                .orElseThrow(() -> new CustomHTTPException(RIOTHTTPErrorResponsesEnum.BOARD_NOT_FOUND)), HttpStatus.OK);
    }

    @PostMapping("/lol/board/{boardId}")
    public ResponseEntity<Void> updateLOLBoardSummoners(@PathVariable String boardId) {
        return updateBoardSummoners(boardId, GameEnum.LOL);
    }

    @PostMapping("/tft/board/{boardId}")
    public ResponseEntity<Void> updateTFTBoardSummoners(@PathVariable String boardId) {
        return updateBoardSummoners(boardId, GameEnum.TFT);
    }

    private ResponseEntity<Void> updateBoardSummoners(String boardId, GameEnum game) {
        LOGGER.info("Updating {} board", game);
        boardService.updateBoardSummoners(boardId, game);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/lol/board")
    public ResponseEntity<String> createLOLBoard() {
        return createBoard(GameEnum.LOL);
    }

    @PostMapping("/tft/board")
    public ResponseEntity<String> createTFTBoard() {
        return createBoard(GameEnum.TFT);
    }

    private ResponseEntity<String> createBoard(GameEnum game) {
        LOGGER.info("Creating {} board", game);
        return new ResponseEntity<>(boardService.createBoard(game), HttpStatus.OK);
    }

    @PostMapping("/lol/board/{boardId}/add/{region}/{tag}/{name}")
    public ResponseEntity<RIOTSummonerEntity> addSummonerToLOLBoard(@PathVariable String boardId, @PathVariable String region, @PathVariable String tag, @PathVariable String name) {
        return addSummonerToBoard(boardId, region, tag, name, GameEnum.LOL);
    }

    @PostMapping("/tft/board/{boardId}/add/{region}/{tag}/{name}")
    public ResponseEntity<RIOTSummonerEntity> addSummonerToTFTBoard(@PathVariable String boardId, @PathVariable String region, @PathVariable String tag, @PathVariable String name) {
        return addSummonerToBoard(boardId, region, tag, name, GameEnum.TFT);
    }

    private ResponseEntity<RIOTSummonerEntity> addSummonerToBoard(String boardId, String region, String tag, String name, GameEnum game) {
        LOGGER.info("Updating {} board with the new summoner", game);
        return new ResponseEntity<>(boardService.addSummonerToBoard(boardId, region, tag, name, game), HttpStatus.OK);
    }

    @PostMapping("/lol/board/{boardId}/remove/{summonerId}")
    public ResponseEntity<Void> removeSummonerOfLOLBoard(@PathVariable String boardId, @PathVariable String summonerId) {
        return removeSummonerOfBoard(boardId, summonerId, GameEnum.LOL);
    }

    @PostMapping("/tft/board/{boardId}/remove/{summonerId}")
    public ResponseEntity<Void> removeSummonerOfTFTBoard(@PathVariable String boardId, @PathVariable String summonerId) {
        return removeSummonerOfBoard(boardId, summonerId, GameEnum.TFT);
    }

    private ResponseEntity<Void> removeSummonerOfBoard(String boardId, String summonerId, GameEnum game) {
        LOGGER.info("Updating {} board without the summoner", game);
        boardService.removeSummonerOfBoard(boardId, summonerId, game);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/lol/board/{boardId}/{name}")
    public ResponseEntity<RIOTSummonerEntity> updateLOLBoardName(@PathVariable String boardId, @PathVariable String name) {
        return updateBoardName(boardId, name, GameEnum.LOL);
    }

    @PostMapping("/tft/board/{boardId}/{name}")
    public ResponseEntity<RIOTSummonerEntity> updateTFTBoardName(@PathVariable String boardId, @PathVariable String name) {
        return updateBoardName(boardId, name, GameEnum.TFT);
    }

    private ResponseEntity<RIOTSummonerEntity> updateBoardName(String boardId, String name, GameEnum game) {
        LOGGER.info("Updating {} board name", game);
        boardService.updateBoardName(boardId, name, game);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}