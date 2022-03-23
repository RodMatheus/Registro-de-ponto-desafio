package br.com.ponto.registro.domain.exception;

public class AplicacaoException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	public AplicacaoException(String message) {
		super(message);
	}
}