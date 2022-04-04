package br.com.ponto.registro.domain.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.com.ponto.registro.domain.model.entities.PontoEletronico;

@Repository
public interface PontoEletronicoRepository extends JpaRepository<PontoEletronico, Long> {

	@Query("FROM PontoEletronico pe "
			+ "WHERE pe.usuario = :usuarioLogado "
			+ "AND DATE(pe.horario) = :dataCorrente")
	Optional<List<PontoEletronico>> findByUsuarioData(final String usuarioLogado, final LocalDate dataCorrente);
}