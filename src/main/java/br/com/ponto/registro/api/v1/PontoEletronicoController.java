package br.com.ponto.registro.api.v1;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.ponto.registro.api.v1.model.input.PostPontoEletronico;
import br.com.ponto.registro.core.security.CheckSecurity;
import br.com.ponto.registro.domain.service.PontoService;
import lombok.extern.slf4j.Slf4j;

@RestController(value = "Controle de horários")
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class PontoEletronicoController {

	private final PontoService pontoService;
	
	@Autowired
	public PontoEletronicoController(PontoService pontoService) {
		this.pontoService = pontoService;
	}
	
	@CheckSecurity.Ponto.podeCriar
	@PostMapping("/batidas")
	public ResponseEntity<Void> post(@Valid @RequestBody final PostPontoEletronico momento) {
		log.info("CADASTRO DE HORÁRIO.");
		
		log.info("Iniciando processo de cadastramento do horário. HORÁRIO: {}", momento);
		pontoService.cadastraPonto(momento.getDataHora(), momento.getDescription());
		
		log.info("Retornando resposta para o cliente.");
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}
}
