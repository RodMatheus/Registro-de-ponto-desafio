package br.com.ponto.registro.test.integracao;

import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import dasniko.testcontainers.keycloak.KeycloakContainer;

@Testcontainers
@DirtiesContext
public class ResourceIT {

	@Container
	public static PostgreSQLContainer<?> postgresDB = new PostgreSQLContainer<>("postgres")
			.withDatabaseName("ponto_eletronico")
			.withUsername("postgres")
			.withPassword("postgres");
	
	@Container
	public static KeycloakContainer keyckloakContainer = new KeycloakContainer("quay.io/keycloak/keycloak:17.0.0")
			.withRealmImportFile("realm-ilia-pontoIT.json")
			.withAdminUsername("admin")
			.withAdminPassword("admin");
		
	@DynamicPropertySource
	public static void properties(DynamicPropertyRegistry registry) {
		registry.add("spring.datasource.url", postgresDB::getJdbcUrl);
		registry.add("spring.datasource.username", postgresDB::getUsername);
		registry.add("spring.datasource.password", postgresDB::getPassword);
		
		registry.add("spring.security.oauth2.resourceserver.jwt.issuer-uri", () -> keyckloakContainer.getAuthServerUrl() + "realms/ilia_ponto");
		registry.add("spring.security.oauth2.resourceserver.jwt.jwk-set-uri", () -> keyckloakContainer.getAuthServerUrl() + "realms/ilia_ponto/protocol/openid-connect/certs");
	}
}