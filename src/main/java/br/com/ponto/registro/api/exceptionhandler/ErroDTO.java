package br.com.ponto.registro.api.exceptionhandler;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@JsonInclude(Include.NON_NULL)
public class ErroDTO {

	private String mensagem;
	private Set<Validacao> validacoes;
	
	public static Validacao ValidacaoOf(final String campo, final String mensagem) {
		return Validacao.builder()
				.campo(campo)
				.mensagem(mensagem)
				.build();
	}
	
	@Builder
	@Getter
	protected static class Validacao {
		private String campo;
		private String mensagem;
	}
}