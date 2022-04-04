package br.com.ponto.registro.test.unidade.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import br.com.ponto.registro.domain.exception.AcessoNegadoException;
import br.com.ponto.registro.domain.exception.ConflitoException;
import br.com.ponto.registro.domain.exception.RecursoNaoEncontradoException;
import br.com.ponto.registro.domain.exception.ValidacaoException;
import br.com.ponto.registro.domain.model.entities.PontoEletronico;
import br.com.ponto.registro.domain.repository.LogAuditoriaRepository;
import br.com.ponto.registro.domain.repository.PontoEletronicoRepository;
import br.com.ponto.registro.domain.service.CadastroPontoEletronicoService;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = CadastroPontoEletronicoService.class)
public class CadastroPontoEletronicoServiceTest {

	private static final LocalDateTime DATA_HORA_FIM_DE_SEMANA_SABADO = LocalDateTime.of(2022, Month.MARCH, 19, 9, 0);
	private static final LocalDateTime DATA_HORA_FIM_DE_SEMANA_DOMINGO = LocalDateTime.of(2022, Month.MARCH, 20, 9, 0);
	private static final LocalDateTime DATA_HORA = LocalDateTime.of(2022, Month.MARCH, 21, 9, 0);
	private static final LocalDateTime DATA_HORA_ALMOCO = LocalDateTime.of(2022, Month.MARCH, 21, 12, 0);

	private static final String DESCRICAO_DE_LANCAMENTO = "Lançando horas!";

	private static final List<PontoEletronico> LISTA_HORARIO_UNICO = List
			.of(PontoEletronico.of("teste", DATA_HORA, "teste"));

	private static final List<PontoEletronico> LISTA_HORARIO_ALMOCO = List.of(
			PontoEletronico.of("teste", DATA_HORA, "teste"), PontoEletronico.of("teste", DATA_HORA_ALMOCO, "teste"));

	private static final List<PontoEletronico> LISTA_LIMITE_MAXIMO = List.of(
			PontoEletronico.of("teste", LocalDateTime.now(), "teste"),
			PontoEletronico.of("teste", LocalDateTime.now(), "teste"),
			PontoEletronico.of("teste", LocalDateTime.now(), "teste"),
			PontoEletronico.of("teste", LocalDateTime.now(), "teste"));
	
	private static final PontoEletronico PONTO_ELETRONICO_RECUPERADO = PontoEletronico.of("teste@teste.com", DATA_HORA, "Lançamento de horas!");

	@MockBean
	private PontoEletronicoRepository pontoEletronicoRepository;

	@MockBean
	private LogAuditoriaRepository logAuditoriaRepository;

	@Mock
	private SecurityContext securityContext;

	@Mock
	private Authentication authentication;

	@Mock
	private Jwt jwt;

	@BeforeEach
	public void setup() {
		SecurityContextHolder.getContext().setAuthentication(authentication);
	}

	@Test
	public void deveRetornarErroDeFimDeSemana_sabado() {
		when((Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).thenReturn(jwt);

		CadastroPontoEletronicoService pontoService = newCadastroPontoEletronicoService();
		Executable executable = () -> pontoService.cadastraPonto(
				DATA_HORA_FIM_DE_SEMANA_SABADO,
				DESCRICAO_DE_LANCAMENTO);

		assertThrows(ValidacaoException.class, executable);
	}

	@Test
	public void deveRetornarErroDeFimDeSemana_domingo() {
		when((Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).thenReturn(jwt);

		CadastroPontoEletronicoService pontoService = newCadastroPontoEletronicoService();
		Executable executable = () -> pontoService.cadastraPonto(
				DATA_HORA_FIM_DE_SEMANA_DOMINGO, DESCRICAO_DE_LANCAMENTO);

		assertThrows(ValidacaoException.class, executable);
	}

	@Test
	public void deveRetornarErroDeLimiteDeLancamentos() {
		when((Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).thenReturn(jwt);
		when(pontoEletronicoRepository.findByUsuarioData(null, DATA_HORA.toLocalDate()))
				.thenReturn(Optional.of(LISTA_LIMITE_MAXIMO));

		CadastroPontoEletronicoService pontoService = newCadastroPontoEletronicoService();
		Executable executable = () -> pontoService.cadastraPonto(DATA_HORA, DESCRICAO_DE_LANCAMENTO);

		assertThrows(AcessoNegadoException.class, executable);
	}

	@Test
	public void deveRetornarErroDeHoraAlmoco() {
		when((Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).thenReturn(jwt);
		when(pontoEletronicoRepository.findByUsuarioData(null, DATA_HORA.toLocalDate()))
				.thenReturn(Optional.of(LISTA_HORARIO_ALMOCO));

		CadastroPontoEletronicoService pontoService = newCadastroPontoEletronicoService();
		Executable executable = () -> pontoService.cadastraPonto(DATA_HORA_ALMOCO.plusMinutes(55),
				DESCRICAO_DE_LANCAMENTO);

		assertThrows(ValidacaoException.class, executable);
	}

	@Test
	public void deveRetornarErroDeHorarioJaCadastrado() {
		when((Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).thenReturn(jwt);
		when(pontoEletronicoRepository.findByUsuarioData(null, DATA_HORA.toLocalDate()))
				.thenReturn(Optional.of(LISTA_HORARIO_UNICO));

		CadastroPontoEletronicoService pontoService = newCadastroPontoEletronicoService();
		Executable executable = () -> pontoService.cadastraPonto(DATA_HORA, DESCRICAO_DE_LANCAMENTO);

		assertThrows(ConflitoException.class, executable);
	}

	@Test
	public void deveCadastrarHorario() {
		when((Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).thenReturn(jwt);
		when(pontoEletronicoRepository.findByUsuarioData(null, DATA_HORA.toLocalDate())).thenReturn(Optional.ofNullable(List.of()));

		CadastroPontoEletronicoService pontoService = newCadastroPontoEletronicoService();
		pontoService.cadastraPonto(DATA_HORA, DESCRICAO_DE_LANCAMENTO);

		verify(pontoEletronicoRepository, times(1)).findByUsuarioData(null, DATA_HORA.toLocalDate());
		verify(pontoEletronicoRepository, times(1)).save(any());
		verify(logAuditoriaRepository, times(1)).save(any());
	}
	
	@Test
	public void deveRetornarErroDeHorarioNaoEncontrado() {
		final LocalDate horario = LocalDate.now();
		
		when((Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).thenReturn(jwt);
		when(pontoEletronicoRepository.findByUsuarioData(null, horario))
				.thenReturn(Optional.of(List.of()));

		CadastroPontoEletronicoService pontoService = newCadastroPontoEletronicoService();
		Executable executable = () -> pontoService.recuperaLancamentos(horario);

		assertThrows(RecursoNaoEncontradoException.class, executable);
	}
	
	@Test
	public void deveRetornarHorario() {
		when((Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).thenReturn(jwt);
		when(pontoEletronicoRepository.findByUsuarioData(null, DATA_HORA.toLocalDate()))
				.thenReturn(Optional.of(List.of(PONTO_ELETRONICO_RECUPERADO)));

		CadastroPontoEletronicoService pontoService = newCadastroPontoEletronicoService();
		List<PontoEletronico> lancamentos = pontoService.recuperaLancamentos(DATA_HORA.toLocalDate());

		assertNotEquals(lancamentos, List.of());
		assertEquals(lancamentos.size(), 1);
		verify(pontoEletronicoRepository, times(1)).findByUsuarioData(null, DATA_HORA.toLocalDate());
	}

	private CadastroPontoEletronicoService newCadastroPontoEletronicoService() {
		return new CadastroPontoEletronicoService(pontoEletronicoRepository, logAuditoriaRepository);
	}
}