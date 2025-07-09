package com.atm.buenas_practicas_java.services.mapper;

import com.atm.buenas_practicas_java.dtos.EtiquetaDto;
import com.atm.buenas_practicas_java.entities.Etiqueta;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class EtiquetaMapper extends AbstractServiceMapper<Etiqueta, EtiquetaDto> {

    @Override
    public EtiquetaDto toDto(Etiqueta entidad) {
        EtiquetaDto dto = new EtiquetaDto();
        new ModelMapper().map(entidad, dto);
        return dto;
    }

    @Override
    public Etiqueta toEntity(EtiquetaDto dto) {
        Etiqueta entidad = new Etiqueta();
        new ModelMapper().map(dto, entidad);
        return entidad;
    }
}

