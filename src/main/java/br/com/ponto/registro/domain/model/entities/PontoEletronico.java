package br.com.ponto.registro.domain.model.entities;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(schema = "ponto", name = "ponto_eletronico")
@Getter
@Setter(value = AccessLevel.PRIVATE)
public class PontoEletronico implements Serializable {

	private static final long serialVersionUID = 1L;

	public PontoEletronico() {}
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotBlank
	@Column(name = "usuario_logado")
	private String usuario;
	
	@NotNull
	private LocalDateTime horario;	
	
	@NotBlank
	private String descricao;
	
	public static PontoEletronico of(final String usuario, final LocalDateTime horario, final String descricao) {
		PontoEletronico ponto = new PontoEletronico();
		ponto.setUsuario(usuario);
		ponto.setHorario(horario);
		ponto.setDescricao(descricao);
		return ponto;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(horario, id, usuario);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PontoEletronico other = (PontoEletronico) obj;
		return Objects.equals(horario, other.horario) && Objects.equals(id, other.id)
				&& Objects.equals(usuario, other.usuario);
	}

	@Override
	public String toString() {
		return "PontoEletronico [id=" + id + ", usuario=" + usuario + ", horario=" + horario + "]";
	}
}
