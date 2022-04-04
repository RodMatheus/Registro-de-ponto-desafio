package br.com.ponto.registro.domain.service;

import java.util.Optional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import br.com.ponto.registro.domain.exception.AplicacaoException;

public abstract class ServiceUtils {

	private ServiceUtils() {
		throw new AssertionError();
	}

	public static final String convertToJson(Object obj) {
		return convertToJsonImpl(obj)
				.orElseThrow(() -> new AplicacaoException("Ocorreu um erro na conversão de dados."));
	}

	private static final Optional<String> convertToJsonImpl(Object obj) {
		try {
			ObjectMapper mapper = new ObjectMapper();
		    mapper.registerModule(new JavaTimeModule());
		    mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
		    
			return Optional.of(mapper.writeValueAsString(obj));
		} catch (JsonProcessingException e) {
			throw new AplicacaoException("Ocorreu um erro ao tentar gerar log de auditoria.");
		}
	}
}