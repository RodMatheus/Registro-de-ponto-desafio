package br.com.ponto.registro.api.v1;

import java.time.LocalDate;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.ponto.registro.api.v1.mapper.PontoEletronicoMapper;
import br.com.ponto.registro.api.v1.model.input.PostPontoEletronico;
import br.com.ponto.registro.api.v1.model.output.PontoEletronicoDTO;
import br.com.ponto.registro.api.v1.openapi.PontoEletronicoControllerOpenApi;
import br.com.ponto.registro.core.security.CheckSecurity;
import br.com.ponto.registro.domain.model.entities.PontoEletronico;
import br.com.ponto.registro.domain.service.CadastroPontoEletronicoService;
import lombok.extern.slf4j.Slf4j;

@RestController(value = "Controle de horários")
@RequestMapping(value = "/batidas", produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class PontoEletronicoController implements PontoEletronicoControllerOpenApi {

	private final CadastroPontoEletronicoService cadastroPontoEletronicoService;
	private final PontoEletronicoMapper pontoEletronicoMapper;
	
	@Autowired
	public PontoEletronicoController(CadastroPontoEletronicoService pontoService,
			PontoEletronicoMapper pontoEletronicoMapper) {
		this.cadastroPontoEletronicoService = pontoService;
		this.pontoEletronicoMapper = pontoEletronicoMapper;
	}
	
	@CheckSecurity.Ponto.podeCriar
	@PostMapping
	public ResponseEntity<Void> post(@Valid @RequestBody final PostPontoEletronico momento) {
		log.info("CADASTRO DE HORÁRIO.");
		
		log.info("Iniciando processo de cadastramento do horário. HORÁRIO: {}.", momento);
		cadastroPontoEletronicoService.cadastraPonto(momento.getDataHora(), momento.getDescription());
		
		log.info("Retornando resposta para o cliente.");
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}
	
	@CheckSecurity.Ponto.podeListar
	@GetMapping
	public ResponseEntity<List<PontoEletronicoDTO>> get(
			@RequestParam final @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data) {
		log.info("LISTAGEM DE LANÇAMENTOS.");

		log.info("Iniciando processo de listagem de lançamentos por data. DATA: {}.", data);
		final List<PontoEletronico> lancamentos = cadastroPontoEletronicoService.recuperaLancamentos(data);

		log.info("Convertendo entidades em DTO. ENTIDADES: {}.", lancamentos);
		final List<PontoEletronicoDTO> dtos = pontoEletronicoMapper.toResourceList(lancamentos);

		log.info("Retornando resposta para o cliente. DTO: {}.", dtos);
		return ResponseEntity.ok(dtos);
	}
}