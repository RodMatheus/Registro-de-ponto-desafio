package br.com.ponto.registro.domain.model.constants;

import java.util.Arrays;

import br.com.ponto.registro.domain.exception.ValidacaoException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum OperacaoAuditoria {

	INCLUSAO(1, "Inclusão");
	
	private final Integer codigo;
	private final String nome;
    
    public static OperacaoAuditoria byNome(String nome) {
        return Arrays
                .stream(values())
                .filter(tipo -> tipo.nome.equalsIgnoreCase(nome))
                .findAny()
                .orElseThrow(() -> new ValidacaoException("O nome informado é inválido!"));
    }
    
    public static OperacaoAuditoria byCodigo(Integer codigo) {
        return Arrays
                .stream(values())
                .filter(tipo -> tipo.codigo.equals(codigo))
                .findAny()
                .orElseThrow(() -> new ValidacaoException("O código informado é inválido!"));
    }
}