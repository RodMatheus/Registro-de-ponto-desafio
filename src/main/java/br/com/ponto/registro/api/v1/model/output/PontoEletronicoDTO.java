package br.com.ponto.registro.api.v1.model.output;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class PontoEletronicoDTO {

	private LocalDateTime horario;
}