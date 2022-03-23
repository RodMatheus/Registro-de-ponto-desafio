package br.com.ponto.registro.domain.model.entities;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import br.com.ponto.registro.domain.model.constants.OperacaoAuditoria;
import br.com.ponto.registro.domain.model.converters.OperacaoAuditoriaConverter;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(schema = "ponto", name = "ponto_eletronico")
@Getter
@Setter(value = AccessLevel.PRIVATE)
public class LogAuditoria implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private LogAuditoria() {}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotBlank
	@Size(max = 255)
	private String evento;
	
	@NotBlank
    @Column(name = "entidade_original")
	private String entidade;
	
	@NotNull
	@Convert(converter = OperacaoAuditoriaConverter.class)
	private OperacaoAuditoria operacao;
	
	@NotNull
	@Column(name = "data_ocorrencia")
	private LocalDateTime dataOcorrencia;
	
	@NotBlank
	@Column(name = "usuario_logado")
	private String usuario;
	
	public static LogAuditoria ofInclusao(final String usuario, final String entidade) {
		
		LocalDateTime horario = LocalDateTime.now();
		StringBuilder evento = new StringBuilder();
		evento.append("Criação de registro de ponto às: ");
		evento.append(horario);
		evento.append(".");
		evento.append(" Operação realizada pelo usuário: ");
		evento.append(usuario);
		
		LogAuditoria log = new LogAuditoria();
		log.setOperacao(OperacaoAuditoria.INCLUSAO);
		log.setEvento(evento.toString());
		log.setDataOcorrencia(horario);
		log.setEntidade(entidade);
		log.setUsuario(usuario);
		
		return log;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(dataOcorrencia, id, entidade, operacao, usuario);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		LogAuditoria other = (LogAuditoria) obj;
		return Objects.equals(dataOcorrencia, other.dataOcorrencia) && Objects.equals(id, other.id)
				&& Objects.equals(entidade, other.entidade) && operacao == other.operacao
				&& Objects.equals(usuario, other.usuario);
	}
	
	@Override
	public String toString() {
		return "LogAuditoria [id=" + id + ", entidade=" + entidade + ", operacao=" + operacao + ", dataOcorrencia="
				+ dataOcorrencia + ", usuario=" + usuario + "]";
	}
}