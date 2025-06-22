package com.krazytop.service.destiny;

import com.krazytop.exception.ExternalServiceException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Base64;

import static com.krazytop.http_responses.ApiErrorEnum.BUNGIE_AUTH_ERROR;

@Service
public class DestinyAuthService {

    @Value("${spring.data.bungie.client_id:'XXX'}")
    private String clientId;
    @Value("${spring.data.bungie.client_secret:'XXX'}")
    private String clientSecret;

    public String getPlayerToken(String playerCode) {
        try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
            HttpPost httpPost = new HttpPost("https://www.bungie.net/Platform/App/OAuth/Token/");
            StringEntity requestEntity = new StringEntity("grant_type=authorization_code&code=" + playerCode);
            return getStringRequest(httpclient, httpPost, requestEntity);
        } catch (IOException e) {
            throw new ExternalServiceException(BUNGIE_AUTH_ERROR);
        }
    }

    public String updatePlayerToken(String refreshPlayerToken) {//TODO voir si enlever les try fonctionne
        try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
            HttpPost httpPost = new HttpPost("https://www.bungie.net/Platform/App/OAuth/Token/");
            StringEntity requestEntity = new StringEntity("grant_type=refresh_token&refresh_token=" + refreshPlayerToken);
            return getStringRequest(httpclient, httpPost, requestEntity);
        } catch (IOException e) {
            throw new ExternalServiceException(BUNGIE_AUTH_ERROR);
        }
    }

    private String getStringRequest(CloseableHttpClient httpclient, HttpPost httpPost, StringEntity requestEntity) throws IOException {
        httpPost.setEntity(requestEntity);
        String auth = String.format("%s:%s", clientId, clientSecret);
        String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes());
        httpPost.addHeader("Authorization", "Basic " + encodedAuth);
        httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded");
        CloseableHttpResponse response = httpclient.execute(httpPost);
        if (response.getStatusLine().getStatusCode() == 200 && response.getEntity() != null) {
            return  EntityUtils.toString(response.getEntity());
        } else {
            throw new ExternalServiceException(BUNGIE_AUTH_ERROR);
        }
    }
}
