package br.com.ponto.registro.api.v1.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import br.com.ponto.registro.api.v1.model.output.PontoEletronicoDTO;
import br.com.ponto.registro.domain.model.entities.PontoEletronico;

@Mapper(componentModel = "spring")
public interface PontoEletronicoMapper {

	List<PontoEletronicoDTO> toResourceList(List<PontoEletronico> pontos);
	
	PontoEletronicoDTO toResource(PontoEletronico ponto);
}