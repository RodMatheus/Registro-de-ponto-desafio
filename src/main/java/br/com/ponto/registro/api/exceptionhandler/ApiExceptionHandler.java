package br.com.ponto.registro.api.exceptionhandler;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import br.com.ponto.registro.api.exceptionhandler.ErroDTO.Validacao;
import br.com.ponto.registro.domain.exception.AcessoNaoAutorizadoException;
import br.com.ponto.registro.domain.exception.AcessoNegadoException;
import br.com.ponto.registro.domain.exception.AplicacaoException;
import br.com.ponto.registro.domain.exception.ConflitoException;
import br.com.ponto.registro.domain.exception.RecursoNaoEncontradoException;
import br.com.ponto.registro.domain.exception.ValidacaoException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

	private final MessageSource messageSource;

	@Autowired
	public ApiExceptionHandler(MessageSource messageSource) {
		this.messageSource = messageSource;
	}

	@ExceptionHandler(Throwable.class)
	public final ResponseEntity<ErroDTO> handleAllExceptions(Throwable ex, WebRequest request) {
		log.error("Erro inesperado na aplicação.", ex);
		String mensagem = "Ocorreu um erro inesperado na aplicação.";
		ErroDTO response = ErroDTO.builder().mensagem(mensagem).build();
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
	}

	@ExceptionHandler(AplicacaoException.class)
	public final ResponseEntity<ErroDTO> handlerAplicacaoExceptions(AplicacaoException ex, WebRequest request) {
		log.error("Ocorreu um erro de na aplicação: ", ex);
		ErroDTO mensagem = ErroDTO.builder().mensagem(ex.getMessage()).build();
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(mensagem);
	}
	
	@ExceptionHandler(ValidacaoException.class)
	public final ResponseEntity<ErroDTO> handlerValidacaoExceptions(ValidacaoException ex, WebRequest request) {
		log.error("Ocorreu um erro de validação: ", ex);
		ErroDTO mensagem = ErroDTO.builder().mensagem(ex.getMessage()).build();
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(mensagem);
	}
	
	@ExceptionHandler(AcessoNaoAutorizadoException.class)
	public final ResponseEntity<ErroDTO> handlerAcessoNaoAutorizadoExceptions(AcessoNaoAutorizadoException ex, WebRequest request) {
		log.error("Ocorreu um erro de autorização: ", ex);
		
		ErroDTO response = ErroDTO.builder().mensagem(ex.getMessage()).build();
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
	}
	
	@ExceptionHandler(AcessoNegadoException.class)
	public final ResponseEntity<ErroDTO> handlerAcessoegadoExceptions(AcessoNegadoException ex, WebRequest request) {
		log.error("Ocorreu um erro de acesso: ", ex);
		
		ErroDTO response = ErroDTO.builder().mensagem(ex.getMessage()).build();
		return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
	}
	
	@ExceptionHandler(ConflitoException.class)
	public final ResponseEntity<ErroDTO> handlerConflitoExceptions(ConflitoException ex, WebRequest request) {
		log.error("Ocorreu um erro de conflito: ", ex);
		
		ErroDTO response = ErroDTO.builder().mensagem(ex.getMessage()).build();
		return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
	}
	
	@ExceptionHandler(RecursoNaoEncontradoException.class)
	public final ResponseEntity<ErroDTO> handlerRecursoNaoEncontradoExceptions(RecursoNaoEncontradoException ex, WebRequest request) {
		log.error("Ocorreu um erro buscar um recurso: ", ex);
		
		ErroDTO response = ErroDTO.builder().mensagem(ex.getMessage()).build();
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
	}
	
	@ExceptionHandler(AccessDeniedException.class)
	public ResponseEntity<ErroDTO> handlerAcessoegadoExceptions(AccessDeniedException ex, WebRequest request) {
		log.error("Ocorreu um erro de acesso: ", ex);
		
		String mensagem = "Você não tem acesso a essa operação.";
		ErroDTO response = ErroDTO.builder().mensagem(mensagem).build();

		return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
	}
	
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {

		log.error("Ocorreram erros de validação: ", ex);
		
		Set<Validacao> validacoes = ex.getBindingResult().getFieldErrors().stream()
				.map(campo -> {
					String mensagem = messageSource.getMessage(campo, LocaleContextHolder.getLocale());
					return ErroDTO.validacaoOf(campo.getField(), mensagem);
					})
				.collect(Collectors.toSet());
		
		ErroDTO response = ErroDTO.builder().validacoes(validacoes).build();
		return ResponseEntity.status(status).body(response);
	}
}
