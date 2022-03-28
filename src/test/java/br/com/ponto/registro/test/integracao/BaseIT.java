package br.com.ponto.registro.test.integracao;

import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.restassured.RestAssured;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BaseIT extends ResourceIT {
	
	private static String URL_TOKEN = "realms/ilia_ponto/protocol/openid-connect/token";
	
	@LocalServerPort
	protected int port;
	
	private String authServerUrl;

    @BeforeEach
    void setup() {
        this.authServerUrl = keyckloakContainer.getAuthServerUrl() + URL_TOKEN;
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;
    }
	
	protected String getToken() {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.put("grant_type", Collections.singletonList("password"));
        map.put("client_id", Collections.singletonList("ponto-client"));
        map.put("username", Collections.singletonList("rodmatheus96@gmail.com"));
        map.put("password", Collections.singletonList("asdf1234"));
        KeyCloakToken token =
                restTemplate.postForObject(
                        authServerUrl, new HttpEntity<>(map, httpHeaders), KeyCloakToken.class);

        assert token != null;
        return token.getAccessToken();
    }
	
	private static class KeyCloakToken {

        private final String accessToken;

        @JsonCreator
        KeyCloakToken(@JsonProperty("access_token") final String accessToken) {
            this.accessToken = accessToken;
        }

        public String getAccessToken() {
            return accessToken;
        }
    }
}