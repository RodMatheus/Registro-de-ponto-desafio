package br.com.ponto.registro.domain.service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import br.com.ponto.registro.core.security.SecurityUtil;
import br.com.ponto.registro.domain.exception.AcessoNegadoException;
import br.com.ponto.registro.domain.exception.ConflitoException;
import br.com.ponto.registro.domain.exception.ValidacaoException;
import br.com.ponto.registro.domain.model.entities.LogAuditoria;
import br.com.ponto.registro.domain.model.entities.PontoEletronico;
import br.com.ponto.registro.domain.repository.LogAuditoriaRepository;
import br.com.ponto.registro.domain.repository.PontoEletronicoRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class PontoEletronicoService {

	private final PontoEletronicoRepository pontoEletronicoRepository;
	private final LogAuditoriaRepository logAuditoriaRepository;

	@Autowired
	public PontoEletronicoService(PontoEletronicoRepository pontoEletronicoRepository,
			 LogAuditoriaRepository logAuditoriaRepository) {
		this.pontoEletronicoRepository = pontoEletronicoRepository;
		this.logAuditoriaRepository = logAuditoriaRepository;
	}

	@Transactional
	public void cadastraPonto(final LocalDateTime horario, final String descricao) {
		final String usuarioLogado = SecurityUtil.getUsuarioLogado();

		log.info("Validando se data informada foi um dia de final de semana.");
		validaFinalSemana(horario);

		log.info("Validando o limite de lançamentos e conflito entre datas cadastradas.");
		validaLimiteAlmocoEConflito(usuarioLogado, horario);

		log.info("Criando entidade de lançamento de ponto.");
		PontoEletronico ponto = PontoEletronico.of(usuarioLogado, horario, descricao);

		log.info("Cadastrando o lançamento. LANÇAMENTO: {}.", ponto);
		pontoEletronicoRepository.save(ponto);
		
		log.info("Gerando log de operação.");
		LogAuditoria log = LogAuditoria.ofInclusao(usuarioLogado, ServiceUtils.convertToJson(ponto));
		logAuditoriaRepository.save(log);
	}

	private void validaFinalSemana(final LocalDateTime horarioInformado) {
		if (horarioInformado.getDayOfWeek().equals(DayOfWeek.SATURDAY)
				|| horarioInformado.getDayOfWeek().equals(DayOfWeek.SUNDAY)) {
			 throw new ValidacaoException("Não é permitido o lançamento de horas durante o final de semana.");
		}
	}

	private void validaLimiteAlmocoEConflito(final String usuarioLogado, final LocalDateTime horarioInformado) {
		final LocalDate dataCorrente = (horarioInformado.getDayOfMonth() < LocalDate.now().getDayOfMonth())
				? horarioInformado.toLocalDate()
				: LocalDate.now();

		final List<PontoEletronico> horarios = pontoEletronicoRepository
				.findByUsuarioData(usuarioLogado,dataCorrente);
		if (!CollectionUtils.isEmpty(horarios)) {
			if (horarios.size() == 4) {
				throw new AcessoNegadoException("Apenas 4 horários podem ser registrados por dia.");
			}

			if (horarios.size() == 2) {
				LocalDateTime pontoAnterior = horarios.get(horarios.size() - 1).getHorario();
				if (ChronoUnit.HOURS.between(pontoAnterior, horarioInformado) <= 0) {
					throw new ValidacaoException("O intervalo de almoço deve ser de pelo menos 1 hora.");
				}
			}

			horarios.stream()
				.filter(horario -> horario.getHorario().isEqual(horarioInformado))
				.findFirst()
				.ifPresent(horario -> {
						throw new ConflitoException("O horário: " + horario.getHorario() + " já foi registrado.");
					});
		}
	}
}
