package br.com.ponto.registro.domain.exception;

public class AcessoNaoAutorizadoException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public AcessoNaoAutorizadoException(String message) {
		super(message);
	}
}