package com.krazytop.service.clash_royal;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.krazytop.repository.clash_royal.CRApiKeyRepository;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class CRApiService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CRApiService.class);

    private final CRApiKeyRepository crApiKeyRepository;

    @Autowired
    public CRApiService(CRApiKeyRepository crApiKeyRepository) {
        this.crApiKeyRepository = crApiKeyRepository;
    }

    public <T> T callCrApi(String apiUrl, Class<T> responseTypeClass) {
        try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
            HttpGet httpGet = new HttpGet(apiUrl);
            httpGet.addHeader("Authorization", "Bearer " + crApiKeyRepository.findFirst());
            try (CloseableHttpResponse response = httpclient.execute(httpGet)) {
                int statusCode = response.getStatusLine().getStatusCode();

                if (statusCode == 200 && response.getEntity() != null) {
                    String responseString = EntityUtils.toString(response.getEntity());
                    return  new ObjectMapper().readValue(responseString, responseTypeClass);
                } else {
                    LOGGER.error("Error in Clash Royal API request");
                    return null;
                }
            }
        } catch (IOException e) {
            return null;
        }
    }

}