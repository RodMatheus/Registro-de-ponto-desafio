package br.com.ponto.registro.api.v1.model.input;

import java.time.LocalDateTime;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Value;

@Builder
@Value
@Schema(name = "Momento")
public class PostPontoEletronico {

	@NotNull(message = "O horário deve ser obrigatório!")
	@PastOrPresent(message = "Não é possível lançar horas futuras")
	private LocalDateTime dataHora;
	
	@NotBlank(message = "A descrição é obrigatória.")
	private String description;
}