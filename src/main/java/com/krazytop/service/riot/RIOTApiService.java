package com.krazytop.service.riot;

import com.krazytop.config.RIOTApiException;
import com.krazytop.http_response.HTTPResponseInterface;
import com.krazytop.nomenclature.GameEnum;
import com.krazytop.repository.api_key.ApiKeyRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class RIOTApiService {

    private final ApiKeyRepository apiKeyRepository;

    @Autowired
    public RIOTApiService(ApiKeyRepository apiKeyRepository) {
        this.apiKeyRepository = apiKeyRepository;
    }

    public <T> T callRiotApi(String apiUrl, Class<? extends HTTPResponseInterface<T>> responseTypeClass) {
        String apiKey = apiKeyRepository.findFirstByGame(GameEnum.RIOT).getKey();
        if (apiUrl.contains("?")) {
            apiUrl += "&";
        } else {
            apiUrl += "?";
        }
        apiUrl += "api_key=" + apiKey;

        try {
            ResponseEntity<? extends HTTPResponseInterface<T>> response = new RestTemplate().getForEntity(apiUrl, responseTypeClass);
            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                return new ModelMapper().map(response.getBody(), responseTypeClass.getDeclaredConstructor().newInstance().getEntityClass());
            } else {
                return null;
            }
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                return null;
            } else {
                throw new RIOTApiException(apiUrl, e);
            }
        } catch (Exception e) {
            throw new RIOTApiException("Error creating responseType instance", e);
        }
    }

    public <T> List<T> callRiotApiForList(String apiUrl, Class<? extends HTTPResponseInterface<T>> responseTypeClass) {
        String apiKey = apiKeyRepository.findFirstByGame(GameEnum.RIOT).getKey();
        if (apiUrl.contains("?")) {
            apiUrl += "&";
        } else {
            apiUrl += "?";
        }
        apiUrl += "api_key=" + apiKey;

        try {
            ResponseEntity<List> response = new RestTemplate().getForEntity(apiUrl, List.class);
            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                List<T> resultList = new ArrayList<>();
                for (Object item : response.getBody()) {
                    resultList.add(new ModelMapper().map(item, responseTypeClass.getDeclaredConstructor().newInstance().getEntityClass()));
                }
                return resultList;
            } else {
                return Collections.emptyList();
            }
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                return Collections.emptyList();
            } else {
                throw new RIOTApiException(apiUrl, e);
            }
        } catch (Exception e) {
            throw new RIOTApiException("Error creating responseType instance", e);
        }
    }


}