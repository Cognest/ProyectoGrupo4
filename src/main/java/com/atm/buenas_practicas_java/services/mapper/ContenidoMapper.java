package com.atm.buenas_practicas_java.services.mapper;

import com.atm.buenas_practicas_java.dtos.ContenidoDto;
import com.atm.buenas_practicas_java.entities.Contenido;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class ContenidoMapper extends AbstractServiceMapper<Contenido, ContenidoDto> {

    @Override
    public ContenidoDto toDto(Contenido entidad) {
        ContenidoDto dto = new ContenidoDto();
        new ModelMapper().map(entidad, dto);
        return dto;
    }

    @Override
    public Contenido toEntity(ContenidoDto dto) {
        Contenido entidad = new Contenido();
        new ModelMapper().map(dto, entidad);
        return entidad;
    }
}

