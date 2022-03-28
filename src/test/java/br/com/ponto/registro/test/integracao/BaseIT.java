package br.com.ponto.registro.test.integracao;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpClient.Redirect;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.time.Duration;

import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;

import br.com.ponto.registro.domain.exception.AplicacaoException;

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
    }
	
	protected String getToken() {        
		try {
			HttpResponse<String> response  = httpClientOAuth()
					.send(
							httpRequestOAuth(this.authServerUrl),
							BodyHandlers.ofString());
			return convertAccessToken(response.body());
		} catch (Exception e) {
			throw new AplicacaoException("Ocorreu um erro ao buscar o Token de acesso.");
		}
    }
	
	private HttpClient httpClientOAuth() {
		return HttpClient.newBuilder()
        .connectTimeout(Duration.ofSeconds(5))
        .followRedirects(Redirect.NORMAL)
        .build();
	}
	
	private HttpRequest httpRequestOAuth(final String server) {
		final String authParams = 
				  "grant_type=password"
                + "&client_id=ponto-client"
                + "&username=rodmatheus96@gmail.com"
                + "&password=asdf1234";
		
		return HttpRequest.newBuilder()
				.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
				.uri(URI.create(server))
				.POST(BodyPublishers.ofString(authParams))
				.build();
	}
	
	private String convertAccessToken(final String body) {
        try {
			JSONObject json = new JSONObject(body);
			return json.getString("access_token");
		} catch (Exception e) {
			throw new AplicacaoException("Ocorreu um erro ao buscar o Token de acesso."); 
		}
	}
}