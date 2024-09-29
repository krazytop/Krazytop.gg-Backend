package com.krazytop.service.lol;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.krazytop.entity.lol.*;
import com.krazytop.repository.lol.LOLMatchRepository;
import com.krazytop.repository.riot.RIOTApiKeyRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.stream.Stream;

@Service
public class LOLMatchService {

    private static final Logger LOGGER = LoggerFactory.getLogger(LOLMatchService.class);

    @Value("${spring.data.web.pageable.default-page-size:5}")
    private int pageSize;
    private final LOLMatchRepository matchRepository;
    private final RIOTApiKeyRepository apiKeyRepository;

    @Autowired
    public LOLMatchService(LOLMatchRepository matchRepository, RIOTApiKeyRepository apiKeyRepository) {
        this.matchRepository = matchRepository;
        this.apiKeyRepository = apiKeyRepository;
    }

    public List<LOLMatchEntity> getLocalMatches(String puuid, int pageNb, String queue, String role) {
        return this.getMatches(puuid, pageNb, queue, role);
    }

    public Long getLocalMatchesCount(String puuid, String queue, String role) {
        return this.getMatchesCount(puuid, queue, role);
    }

    private void updateMatch(String matchId, String puuid) throws URISyntaxException, IOException {
        String stringUrl = String.format("https://europe.api.riotgames.com/lol/match/v5/matches/%s?api_key=%s", matchId, apiKeyRepository.findFirstByOrderByKeyAsc().getKey());
        ObjectMapper mapper = new ObjectMapper();
        JsonNode infoNode = mapper.readTree(new URI(stringUrl).toURL()).get("info");
        LOLMatchEntity match = mapper.convertValue(infoNode, LOLMatchEntity.class);
        match.setId(matchId);
        match.getOwners().add(puuid);
        match.dispatchParticipantsInTeamsAndBuildSummoners();
        match.setRemake(match.getTeams().get(0).getParticipants().get(0).isGameEndedInEarlySurrender());
        if (this.checkIfQueueIsCompatible(match)) {
            LOGGER.info("Saving match : {}", matchId);
            matchRepository.save(match);
        }
    }

    /**
     * Due to development API Key rate limit, we recover only and always 100 last matches
     */
    public void updateRemoteToLocalMatches(String puuid) throws IOException {
        try {
            String stringUrl = String.format("https://europe.api.riotgames.com/lol/match/v5/matches/by-puuid/%s/ids?start=%d&count=%d&api_key=%s", puuid, 0, 100, apiKeyRepository.findFirstByOrderByKeyAsc().getKey());
            ObjectMapper mapper = new ObjectMapper();
            JsonNode json = mapper.readTree(new URI(stringUrl).toURL());
            List<String> matchIds = mapper.convertValue(json, new TypeReference<>() {});
            for (String matchId : matchIds) {
                LOLMatchEntity existingMatch = this.matchRepository.findFirstById(matchId);
                if (existingMatch == null) {
                    this.updateMatch(matchId, puuid);
                    Thread.sleep(2000);
                } else if (!existingMatch.getOwners().contains(puuid)) {
                    existingMatch.getOwners().add(puuid);
                    LOGGER.info("Updating match : {}", matchId);
                    matchRepository.save(existingMatch);
                } else {
                    break;
                }
            }
        } catch (InterruptedException | URISyntaxException | IOException e) {
            LOGGER.error("Error while updating matches : {}", e.getMessage());
            Thread.currentThread().interrupt();
            throw new IOException(e);
        }
    }

    private boolean checkIfQueueIsCompatible(LOLMatchEntity match) {
        List<String> compatibleQueues = Stream.of("normal", "solo-ranked", "flex-ranked", "aram", "urf", "nexus-blitz", "one-for-all", "ultimate-spellbook")
                .map(this::getQueueIds)
                .flatMap(List::stream)
                .toList();
        return compatibleQueues.contains(match.getQueue().getId());
    }

    public List<LOLMatchEntity> getMatches(String puuid, int pageNb, String queue, String role) {
        PageRequest pageRequest = PageRequest.of(pageNb, pageSize);
        if (queue.equals("all-queues")) {
            if (role.equals("all-roles")) {
                return this.matchRepository.findAll(puuid, pageRequest).getContent();
            } else {
                return this.matchRepository.findAllByRole(puuid, getRole(role), pageRequest).getContent();
            }
        } else {
            if (role.equals("all-roles")) {
                return this.matchRepository.findAllByQueue(puuid, getQueueIds(queue), pageRequest).getContent();
            } else {
                return this.matchRepository.findAllByQueueAndByRole(puuid, getQueueIds(queue), getRole(role), pageRequest).getContent();
            }
        }
    }

    public Long getMatchesCount(String puuid, String queue, String role) {
        if (queue.equals("all-queues")) {
            if (role.equals("all-roles")) {
                return this.matchRepository.countAll(puuid);
            } else {
                return this.matchRepository.countAllByRole(puuid, getRole(role));
            }
        } else {
            if (role.equals("all-roles")) {
                return this.matchRepository.countAllByQueue(puuid, getQueueIds(queue));
            } else {
                return this.matchRepository.countAllByQueueAndByRole(puuid, getQueueIds(queue), getRole(role));
            }
        }
    }

    private List<String> getQueueIds(String queueName) {
        return switch (queueName) {
            case "normal" -> List.of("14", "61", "400", "2", "430", "490");
            case "solo-ranked" -> List.of("4", "420");
            case "flex-ranked" -> List.of("6", "42", "440");
            case "aram" -> List.of("65", "100", "450");
            case "urf" -> List.of("76", "1900", "318", "1010");
            case "nexus-blitz" -> List.of("1200", "1300");
            case "one-for-all" -> List.of("70", "1020");
            case "ultimate-spellbook" -> List.of("1400");
            case "arena" -> List.of("1700", "1710");
            default -> List.of();
        };
    }

    private String getRole(String roleName) {
        return switch (roleName) {
            case "top" -> "TOP";
            case "jungle" -> "JUNGLE";
            case "middle" -> "MIDDLE";
            case "bottom" -> "BOTTOM";
            case "support" -> "UTILITIES";
            default -> "";
        };
    }

}
