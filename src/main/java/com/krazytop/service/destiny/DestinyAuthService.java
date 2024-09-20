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

    @Value("${spring.data.bungie.client_id:'XXX'}")
    private String clientId;
    @Value("${spring.data.bungie.client_secret:'XXX'}")
    private String clientSecret;

    public String getPlayerToken(String playerCode) throws IOException {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost("https://www.bungie.net/Platform/App/OAuth/Token/");
        StringEntity requestEntity = new StringEntity("grant_type=authorization_code&code=" + playerCode);
        return getStringRequest(httpclient, httpPost, requestEntity);
    }

    public String updatePlayerToken(String refreshPlayerToken) throws IOException {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost("https://www.bungie.net/Platform/App/OAuth/Token/");
        StringEntity requestEntity = new StringEntity("grant_type=refresh_token&refresh_token=" + refreshPlayerToken);
        return getStringRequest(httpclient, httpPost, requestEntity);
    }

    private String getStringRequest(CloseableHttpClient httpclient, HttpPost httpPost, StringEntity requestEntity) throws IOException {
        httpPost.setEntity(requestEntity);
        String auth = clientId + ":" + clientSecret;

        String encodedAuth = java.util.Base64.getEncoder().encodeToString(auth.getBytes());
        httpPost.addHeader("Authorization", "Basic " + encodedAuth);
        httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded");
        CloseableHttpResponse response = httpclient.execute(httpPost);
        int statusCode = response.getStatusLine().getStatusCode();
        if (statusCode == 200 && response.getEntity() != null) {
            return  EntityUtils.toString(response.getEntity());
        } else {
            throw new IOException(String.format("Error while retrieving or refreshing Bungie player token : %s", response));
        }
    }
}
