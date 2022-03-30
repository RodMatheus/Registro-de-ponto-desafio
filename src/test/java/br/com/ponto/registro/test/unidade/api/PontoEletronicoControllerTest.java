package br.com.ponto.registro.test.unidade.api;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.time.LocalDateTime;
import java.time.Month;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import br.com.ponto.registro.api.v1.PontoEletronicoController;
import br.com.ponto.registro.api.v1.model.input.PostPontoEletronico;
import br.com.ponto.registro.domain.exception.ValidacaoException;
import br.com.ponto.registro.domain.service.PontoEletronicoService;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = PontoEletronicoController.class)
public class PontoEletronicoControllerTest {

	private static final PostPontoEletronico POST_PONTO_ELETRONICO = PostPontoEletronico.builder()
			.dataHora(LocalDateTime.of(2022, Month.MARCH, 19, 9, 0))
			.description("Lançando horas!")
			.build();
	
	private static final PostPontoEletronico POST_PONTO_ELETRONICO_FINAL_DE_SEMANA = PostPontoEletronico.builder()
			.dataHora(LocalDateTime.of(2022, Month.MARCH, 19, 9, 0))
			.description("Lançando horas!")
			.build();

	@MockBean
	private PontoEletronicoService pontoService;

	@Test
	public void deveCadastrarHorario() {
		PontoEletronicoController pontoEletronicoController = newPontoEletronicoController();
		
		pontoEletronicoController.post(POST_PONTO_ELETRONICO);
		
		verify(pontoService, times(1)).cadastraPonto(POST_PONTO_ELETRONICO.getDataHora(),
				POST_PONTO_ELETRONICO.getDescription());
	}
	
	@Test
	public void deveRetornarValidacaoFimDeSemana() {
		PontoEletronicoController pontoEletronicoController = newPontoEletronicoController();
		doThrow(ValidacaoException.class).when(pontoService).cadastraPonto(
				POST_PONTO_ELETRONICO_FINAL_DE_SEMANA.getDataHora(),
				POST_PONTO_ELETRONICO_FINAL_DE_SEMANA.getDescription());

		Executable executable = () -> pontoEletronicoController.post(POST_PONTO_ELETRONICO_FINAL_DE_SEMANA);

		assertThrows(ValidacaoException.class, executable);

		verify(pontoService, times(1)).cadastraPonto(POST_PONTO_ELETRONICO_FINAL_DE_SEMANA.getDataHora(),
				POST_PONTO_ELETRONICO_FINAL_DE_SEMANA.getDescription());
	}


	private PontoEletronicoController newPontoEletronicoController() {
		return new PontoEletronicoController(pontoService);
	}
}
