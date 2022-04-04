package br.com.ponto.registro.test.integracao;

import static io.restassured.RestAssured.given;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.jdbc.Sql;

import io.restassured.http.ContentType;

@TestMethodOrder(OrderAnnotation.class)
public class PontoEletronicoControllerIT extends BaseIT {

	private static final String BASE_PATH = "/batidas";
	
	@Test
	@Order(1)
	public void postPontoEletroniconNaoAutorizado() {
		given()
			.basePath(BASE_PATH)
			.port(port)
			.contentType(ContentType.JSON)
			.body("{\"dataHora\":\"2022-03-23T19:30:25\",\"description\":\"Éhoradoduelo4\"}")
		.when()
			.post()
		.then()
			.statusCode(HttpStatus.UNAUTHORIZED.value());
	}
	
	@Test
	@Order(2)
	public void postPontoEletronicoCorreto() {
		given()
			.basePath(BASE_PATH)
			.port(port)
			.contentType(ContentType.JSON)
			.auth()
				.oauth2(TOKEN)
			.body("{\"dataHora\":\"2022-03-23T19:30:25\",\"description\":\"Éhoradoduelo4\"}")
		.when()
			.post()
		.then()
			.statusCode(HttpStatus.CREATED.value());
	}
	
	@Test
	@Order(3)
	public void postPontoEletronicoConflito() {
		given()
			.basePath(BASE_PATH)
			.port(port)
			.contentType(ContentType.JSON)
			.auth()
				.oauth2(TOKEN)
			.body("{\"dataHora\":\"2022-03-23T19:30:25\",\"description\":\"Éhoradoduelo4\"}")
		.when()
			.post()
		.then()
			.statusCode(HttpStatus.CONFLICT.value());
	}
	
	@Test
	@Order(4)
	public void postPontoEletronicoFinalDeSemana() {
		given()
			.basePath(BASE_PATH)
			.port(port)
			.contentType(ContentType.JSON)
			.auth()
				.oauth2(TOKEN)
			.body("{\"dataHora\":\"2022-03-19T19:30:25\",\"description\":\"Éhoradoduelo4\"}")
		.when()
			.post()
		.then()
			.statusCode(HttpStatus.BAD_REQUEST.value());
	}
	
	@Test
	@Order(5)
	@Sql(scripts = "/sql/insert_data_teste_limite_lista.sql")
	public void postPontoEletronico() {
		given()
			.basePath(BASE_PATH)
			.port(port)
			.contentType(ContentType.JSON)
			.auth()
				.oauth2(TOKEN)
			.body("{\"dataHora\":\"2022-03-18T20:30:25\",\"description\":\"Éhoradoduelo4\"}")
		.when()
			.post()
		.then()
			.statusCode(HttpStatus.FORBIDDEN.value());
	}
}