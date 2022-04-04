package br.com.ponto.registro.domain.model.converters;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import br.com.ponto.registro.domain.model.constants.OperacaoAuditoria;

@Converter
public class OperacaoAuditoriaConverter implements AttributeConverter<OperacaoAuditoria, Integer>{

	@Override
	public Integer convertToDatabaseColumn(OperacaoAuditoria operacao) {
		return operacao.getCodigo();
	}

	@Override
	public OperacaoAuditoria convertToEntityAttribute(Integer codigo) {
		return OperacaoAuditoria.byCodigo(codigo);
	}
}