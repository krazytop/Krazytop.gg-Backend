package com.krazytop.service.destiny;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.krazytop.api_gateway.model.generated.DestinyAuthTokensDTO;
import com.krazytop.exception.CustomException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
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

    public DestinyAuthTokensDTO getPlayerTokens(String playerCode) {
        return getPlayerTokensRequest("grant_type=authorization_code&code=" + playerCode);
    }

    public DestinyAuthTokensDTO updatePlayerTokens(String refreshPlayerToken) {
        return getPlayerTokensRequest("grant_type=refresh_token&refresh_token=" + refreshPlayerToken);
    }

    private DestinyAuthTokensDTO getPlayerTokensRequest(String requestBody) {
        try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
            HttpPost httpPost = new HttpPost("https://www.bungie.net/Platform/App/OAuth/Token/");
            httpPost.setEntity(new StringEntity(requestBody, ContentType.APPLICATION_FORM_URLENCODED));
            String auth = String.format("%s:%s", clientId, clientSecret);
            String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes());
            httpPost.addHeader("Authorization", "Basic " + encodedAuth);
            CloseableHttpResponse response = httpclient.execute(httpPost);
            if (response.getStatusLine().getStatusCode() == 200 && response.getEntity() != null) {
                return new ObjectMapper().readValue(EntityUtils.toString(response.getEntity()), DestinyAuthTokensDTO.class);
            } else {
                throw new CustomException(BUNGIE_AUTH_ERROR);
            }
        } catch (IOException ex) {
            throw new CustomException(BUNGIE_AUTH_ERROR, ex);
        }
    }
}
