package br.com.ponto.registro.core.openapi;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class OpenApiConfig {

	@Bean
	public OpenAPI pontoEletronicoOpenApi() {
		return new OpenAPI()
				.info(apiInfo());
	}
	
    private Info apiInfo() {
        return new Info().title("Ponto Eletrônico API")
                .description("API registro de ponto eletrônico")
                .version("v0.0.1")
                .contact(new Contact()
                        .name("Ília Digital Examplo")
                        .url("https://www.ilia.digital")
                        .email("example@ilia.digital"));
    }
}