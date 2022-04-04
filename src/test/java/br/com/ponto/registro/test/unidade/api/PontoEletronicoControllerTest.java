package br.com.ponto.registro.test.unidade.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import br.com.ponto.registro.api.v1.PontoEletronicoController;
import br.com.ponto.registro.api.v1.mapper.PontoEletronicoMapper;
import br.com.ponto.registro.api.v1.model.input.PostPontoEletronico;
import br.com.ponto.registro.api.v1.model.output.PontoEletronicoDTO;
import br.com.ponto.registro.domain.exception.RecursoNaoEncontradoException;
import br.com.ponto.registro.domain.exception.ValidacaoException;
import br.com.ponto.registro.domain.model.entities.PontoEletronico;
import br.com.ponto.registro.domain.service.CadastroPontoEletronicoService;

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
	
	
	private static final LocalDateTime HORARIO_PONTO_ELETRONICO_RECUPERADO = LocalDateTime.of(2022, Month.MARCH, 30, 9, 0);
	private static final PontoEletronico PONTO_ELETRONICO_RECUPERADO = PontoEletronico.of("teste@teste.com", HORARIO_PONTO_ELETRONICO_RECUPERADO, "Lançamento de horas!");
	private static final List<PontoEletronico> LISTA_PONTO_ELETRONICO_RECUPERADO = List.of(PONTO_ELETRONICO_RECUPERADO);
	private static final List<PontoEletronicoDTO> LISTA_DTO_PONTO_ELETRONICO = List.of(PontoEletronicoDTO.builder().horario(HORARIO_PONTO_ELETRONICO_RECUPERADO).build());


	@MockBean
	private CadastroPontoEletronicoService pontoService;
	
	@MockBean
	private PontoEletronicoMapper pontoEletronicoMapper;

	@Test
	public void deveCadastrarHorario() {
		PontoEletronicoController pontoEletronicoController = newPontoEletronicoController();
		pontoEletronicoController.post(POST_PONTO_ELETRONICO);
		
		verify(pontoService, times(1)).cadastraPonto(POST_PONTO_ELETRONICO.getDataHora(),
				POST_PONTO_ELETRONICO.getDescription());
	}
	
	@Test
	public void deveRetornarErroFimDeSemana_post() {
		doThrow(ValidacaoException.class).when(pontoService).cadastraPonto(
				POST_PONTO_ELETRONICO_FINAL_DE_SEMANA.getDataHora(),
				POST_PONTO_ELETRONICO_FINAL_DE_SEMANA.getDescription());
		
		PontoEletronicoController pontoEletronicoController = newPontoEletronicoController();
		Executable executable = () -> pontoEletronicoController.post(POST_PONTO_ELETRONICO_FINAL_DE_SEMANA);

		assertThrows(ValidacaoException.class, executable);
		verify(pontoService, times(1)).cadastraPonto(POST_PONTO_ELETRONICO_FINAL_DE_SEMANA.getDataHora(),
				POST_PONTO_ELETRONICO_FINAL_DE_SEMANA.getDescription());
	}
	
	@Test
	public void deveRetornarListaHorarios_get() {
		final LocalDate horario = LocalDate.now();
		
		when(pontoService.recuperaLancamentos(horario)).thenReturn(LISTA_PONTO_ELETRONICO_RECUPERADO);
		when(pontoEletronicoMapper.toResourceList(LISTA_PONTO_ELETRONICO_RECUPERADO)).thenReturn(LISTA_DTO_PONTO_ELETRONICO);
		
		PontoEletronicoController pontoEletronicoController = newPontoEletronicoController();
		ResponseEntity<List<PontoEletronicoDTO>> lancamentos = pontoEletronicoController.get(horario);

		assertNotEquals(lancamentos.getBody(), List.of());
		assertEquals(lancamentos.getBody().size(), 1);
		verify(pontoService, times(1)).recuperaLancamentos(horario);
		verify(pontoEletronicoMapper, times(1)).toResourceList(LISTA_PONTO_ELETRONICO_RECUPERADO);
	}
	
	@Test
	public void deveRetornarErroDeHorarioNaoEncontrado_get() {
		final LocalDate horario = LocalDate.now();
		
		when(pontoService.recuperaLancamentos(horario)).thenThrow(RecursoNaoEncontradoException.class);

		PontoEletronicoController pontoEletronicoController = newPontoEletronicoController();
		Executable executable = () -> pontoEletronicoController.get(horario);

		assertThrows(RecursoNaoEncontradoException.class, executable);
		verify(pontoService, times(1)).recuperaLancamentos(horario);
	}


	private PontoEletronicoController newPontoEletronicoController() {
		return new PontoEletronicoController(pontoService, pontoEletronicoMapper);
	}
}