package com.krazytop.service.destiny;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class DestinyAuthService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DestinyAuthService.class);

    @Value("${spring.data.bungie.client_id:'XXX'}")
    private String CLIENT_ID;
    @Value("${spring.data.bungie.client_secret:'XXX'}")
    private String CLIENT_SECRET;

    public String getPlayerToken(String playerCode) {
        try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
            HttpPost httpPost = new HttpPost("https://www.bungie.net/Platform/App/OAuth/Token/");
            StringEntity requestEntity = new StringEntity("grant_type=authorization_code&code=" + playerCode);
            return getStringRequest(httpclient, httpPost, requestEntity);
        } catch (IOException e) {
            return null;
        }
    }

    public String updatePlayerToken(String refreshPlayerToken) {
        try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
            HttpPost httpPost = new HttpPost("https://www.bungie.net/Platform/App/OAuth/Token/");
            StringEntity requestEntity = new StringEntity("grant_type=refresh_token&refresh_token=" + refreshPlayerToken);
            return getStringRequest(httpclient, httpPost, requestEntity);
        } catch (IOException e) {
            return null;
        }
    }

    private String getStringRequest(CloseableHttpClient httpclient, HttpPost httpPost, StringEntity requestEntity) throws IOException {
        httpPost.setEntity(requestEntity);
        String auth = CLIENT_ID + ":" + CLIENT_SECRET;
        String encodedAuth = java.util.Base64.getEncoder().encodeToString(auth.getBytes());
        httpPost.addHeader("Authorization", "Basic " + encodedAuth);
        httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded");
        try (CloseableHttpResponse response = httpclient.execute(httpPost)) {
            int statusCode = response.getStatusLine().getStatusCode();

            if (statusCode == 200 && response.getEntity() != null) {
                return  EntityUtils.toString(response.getEntity());
            } else {
                LOGGER.error("Error while retrieve Bungie player token : {}", response);
                return null;
            }
        }
    }
}
