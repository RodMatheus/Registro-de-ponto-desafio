package br.com.ponto.registro.test.unidade.api;

import java.time.LocalDateTime;
import java.time.Month;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import br.com.ponto.registro.api.v1.PontoEletronicoController;
import br.com.ponto.registro.api.v1.model.input.PostPontoEletronico;
import br.com.ponto.registro.domain.service.PontoService;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = PontoEletronicoController.class)
public class PontoEletronicoControllerTest {
	
	private static final PostPontoEletronico POST_PONTO_ELETRONICO = PostPontoEletronico.builder()
			.dataHora(LocalDateTime.of(2022, Month.MARCH, 21, 9, 0))
			.description("Lançando horas!")
			.build();
	
	@MockBean
	private PontoService pontoService;
	
	@Test
	public void deveCadastrarHorario() {
		PontoEletronicoController pontoEletronicoController = newPontoEletronicoController();
		
		pontoEletronicoController.post(POST_PONTO_ELETRONICO);
	}
	
	private PontoEletronicoController newPontoEletronicoController() {
		return new PontoEletronicoController(pontoService);
	}
}
