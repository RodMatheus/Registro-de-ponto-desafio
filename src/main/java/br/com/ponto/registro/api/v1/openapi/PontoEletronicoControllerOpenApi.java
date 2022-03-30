package br.com.ponto.registro.api.v1.openapi;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import br.com.ponto.registro.api.exceptionhandler.ErroDTO;
import br.com.ponto.registro.api.v1.model.input.PostPontoEletronico;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Ponto Eletrônico")
public interface PontoEletronicoControllerOpenApi {

	@Operation(summary = "Lança um registro de ponto", responses = {
			@ApiResponse(responseCode = "201", description = "Recurso criado com sucesso.", content = {
					@Content(mediaType = MediaType.APPLICATION_JSON_VALUE) }),
			@ApiResponse(responseCode = "400", description = "Requisição inválida.", content = {
					@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErroDTO.class)) }),
			@ApiResponse(responseCode = "401", description = "Você não tem autorização para executar esta operação.", content = {
					@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErroDTO.class)) }),
			@ApiResponse(responseCode = "403", description = "Você não tem acesso a esta operação.", content = {
					@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErroDTO.class)) }),
			@ApiResponse(responseCode = "409", description = "Existe alguma informação conflitante enviada.", content = {
					@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErroDTO.class)) }),
			@ApiResponse(responseCode = "500", description = "Ocorreu um erro interno no servidor.", content = {
					@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErroDTO.class)) }), })
	public ResponseEntity<Void> post(
			@Parameter(description = "Representação do horário a ser lançado", required = true) final PostPontoEletronico momento);
}